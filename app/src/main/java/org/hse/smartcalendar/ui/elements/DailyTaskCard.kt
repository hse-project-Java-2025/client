package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask


@Composable
fun DailyTaskCard(task : DailyTask, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(5.dp)) {

    Surface(
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        tonalElevation = 2.dp,
        shadowElevation = 10.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
                Text(
                    text = task.getDailyTaskTitle(),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight(400),
                    modifier = Modifier.weight(3f)
                )
                Text(
                    text = task.getDailyTaskSchedule(),
                    modifier = Modifier.weight(2f)
                        .padding(10.dp)
                        .align(Alignment.Bottom),
                    textAlign = TextAlign.End
                )

            }

        }

        Surface(
            color = MaterialTheme.colorScheme.surface,
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            tonalElevation = 2.dp,
            shadowElevation = 10.dp,
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            Row (modifier = Modifier.fillMaxWidth().padding(10.dp)) {
                Text(
                    text = task.getDailyTaskDescription(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }

}


@Composable
@Preview
fun DailyTaskCardPreview() {
    val previewTask = DailyTask(
        title = "Пример задачи",
        description = "Пример описания",
        duration = LocalTime(
            hour = 1,
            minute = 0),
        start = LocalTime(
            hour = 4,
            minute = 0
        )
    )

    DailyTaskCard(
        task = previewTask
    )
}