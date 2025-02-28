package org.hse.smartcalendar.ui.elements

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.datetime.LocalTime
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.view.model.ListViewModel

@Composable
fun DailyTasksList(viewModel: ListViewModel) {
    val dailyTaskListState = viewModel.dailyTaskListFlow.collectAsStateWithLifecycle()

    LazyColumn {
        items(dailyTaskListState.value) {
            DailyTaskCard(it)
        }
    }
}

@Composable
@Preview
fun DailyTaskListPreview() {
    val viewModel = ListViewModel()

    for (i  in 1..10) {
        val previewTask = DailyTask(
            title = "Task title",
            description = "Task description",
            duration = LocalTime(
                hour = 1,
                minute = 0),
            start = LocalTime(
                hour = 4 + i,
                minute = 0
            )
        )

        viewModel.addDailyTask(previewTask)
    }

    DailyTasksList(viewModel)
}