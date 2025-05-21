package org.hse.smartcalendar.ui.elements

import android.Manifest
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import org.hse.smartcalendar.R
import org.hse.smartcalendar.utility.RecordState
import org.hse.smartcalendar.utility.startRecord
import org.hse.smartcalendar.utility.stopRecord
import java.io.File

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AudioRecorderButton(
    modifier: Modifier = Modifier,
    audioFile: MutableState<File?>,
    onStop: () -> Unit
) {
    val recordingState = rememberSaveable { mutableStateOf(RecordState.IDLE) }
    val context = LocalContext.current
    val audioPermissionState = rememberPermissionState(Manifest.permission.RECORD_AUDIO)

    Button(
        modifier = modifier,
        onClick = {
            when {
                !audioPermissionState.status.isGranted -> {
                    audioPermissionState.launchPermissionRequest()
                }

                recordingState.value == RecordState.IDLE -> {
                    startRecord(context) { file ->
                        audioFile.value = file
                    }
                    recordingState.value = RecordState.RECORDING
                }

                else -> {
                    recordingState.value = RecordState.LOADING
                    stopRecord()
                    onStop()
                    recordingState.value = RecordState.IDLE
                }
            }
        },
        enabled = recordingState.value != RecordState.LOADING,
    ) {
        when (recordingState.value) {
            RecordState.IDLE ->
                Icon(
                    painter = painterResource(R.drawable.mic_asset),
                    contentDescription = "Recording",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )

            RecordState.RECORDING ->
                Icon(
                    painter = painterResource(R.drawable.stop_asset),
                    contentDescription = "Recording",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )

            RecordState.LOADING ->
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp),
                    color = Color.Red,
                    strokeWidth = 2.dp
                )
        }
    }
}

@Preview
@Composable
fun AudioRecorderButtonPreview() {
    val audioFile: MutableState<File?> = rememberSaveable { mutableStateOf(null) }
    AudioRecorderButton(onStop = {}, audioFile = audioFile)
}