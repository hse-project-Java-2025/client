package org.hse.smartcalendar.utility

import android.content.Context
import android.media.MediaRecorder
import android.util.Log
import java.io.File

private var mediaRecorder: MediaRecorder? = null

fun startRecord(context: Context, onFileCreated: (File) -> Unit) {
    try {
        val outputFile = File(
            context.getExternalFilesDir(null),
            "audio_record_${System.currentTimeMillis()}.mp3"
        )

        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setOutputFile(outputFile.absolutePath)
            prepare()
            start()
        }

        onFileCreated(outputFile)
    } catch (e: Exception) {
        Log.e("AudioRecorder", "Recording failed", e)
    }
}

fun stopRecord() {
    mediaRecorder?.apply {
        try {
            stop()
            release()
        } catch (e: Exception) {
            Log.e("AudioRecorder", "Stop failed", e)
        }
    }
    mediaRecorder = null
}