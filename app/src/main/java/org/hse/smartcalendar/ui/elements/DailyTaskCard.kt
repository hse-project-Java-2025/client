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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType


@Composable
fun DailyTaskCard(task : DailyTask, modifier: Modifier = Modifier) {
    Column(modifier = Modifier.padding(5.dp)) {

    Surface(
        color = getCardTitleColor(task),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
        tonalElevation = 2.dp,
        shadowElevation = 10.dp,
        modifier = modifier
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = task.getDailyTaskTitle(),
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight(400),
                modifier = Modifier.weight(3f)
            )
            Text(
                text = task.getDailyTaskArrangementString(),
                modifier = Modifier
                    .weight(2f)
                    .padding(10.dp)
                    .align(Alignment.Bottom),
                textAlign = TextAlign.End
            )
        }
    }
        Surface(
            color = getCardDescriptionColor(task),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
            tonalElevation = 2.dp,
            shadowElevation = 10.dp,
            modifier = modifier
        ) {
            Spacer(modifier = Modifier.height(42.dp))
            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)) {
                Text(
                    text = task.getDailyTaskDescription(),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}


@Composable
@Preview(showBackground = true)
fun DailyTaskCardPreview() {
    val previewCommonTask = DailyTask(
        title = "Common title example",
        type = DailyTaskType.COMMON,
        description = "Common description Example",
        duration = LocalTime(
            hour = 1,
            minute = 0),
        start = LocalTime(
            hour = 4,
            minute = 0
        )
    )


    val previewFitnessTask = DailyTask(
        title = "Fitness title example",
        type = DailyTaskType.FITNESS,
        description = "Fitness description Example",
        duration = LocalTime(
            hour = 1,
            minute = 0
        ),
        start = LocalTime(
            hour = 4,
            minute = 0
        )
    )

    val previewWorkTask = DailyTask(
        title = "Work title example",
        type = DailyTaskType.WORK,
        description = "Work description Example",
        duration = LocalTime(
            hour = 1,
            minute = 0
        ),
        start = LocalTime(
            hour = 4,
            minute = 0
        )
    )

    val previewStudiesTask = DailyTask(
        title = "Studies title example",
        type = DailyTaskType.STUDIES,
        description = "Studies description Example",
        duration = LocalTime(
            hour = 1,
            minute = 0
        ),
        start = LocalTime(
            hour = 4,
            minute = 0
        )
    )

    Column {
        DailyTaskCard(
            task = previewCommonTask
        )
        DailyTaskCard(
            task = previewFitnessTask
        )
        DailyTaskCard(
            task = previewWorkTask
        )
        DailyTaskCard(
            task = previewStudiesTask
        )
    }
}

fun getCardTitleColor(task: DailyTask): Color {
    return when (task.getDailyTaskType()) {
        DailyTaskType.FITNESS -> Color.Red
        DailyTaskType.WORK -> Color.Gray
        DailyTaskType.STUDIES -> Color.Blue
        else -> Color.White
    }
}


fun getCardDescriptionColor(task: DailyTask): Color {
    return when (task.getDailyTaskType()) {
        DailyTaskType.FITNESS -> Color.Red
        DailyTaskType.WORK -> Color.DarkGray
        DailyTaskType.STUDIES -> Color.Green
        else -> Color.White
    }
}