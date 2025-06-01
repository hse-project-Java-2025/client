package org.hse.smartcalendar.network

import org.hse.smartcalendar.data.TotalTimeTaskTypes
import org.hse.smartcalendar.view.model.StatisticsViewModel

data class StatisticsResponse(
    val totalTime: TotalTime,
    val weekTime: Long,
    val todayTime: TodayTime,
    val continuesSuccessDays: ContinuesSuccessDays,
    val averageDayTime: AverageDayTime
) {
    companion object {
        fun fromViewModel(viewModel: StatisticsViewModel): StatisticsResponse {
            val totalTime = viewModel.getTotalTimeActivityTypes()

            return StatisticsResponse(
                totalTime = TotalTime(
                    common = totalTime.Common.time.inWholeMinutes,
                    work = totalTime.Work.time.inWholeMinutes,
                    study = totalTime.Study.time.inWholeMinutes,
                    fitness = totalTime.Fitness.time.inWholeMinutes
                ),
                weekTime = viewModel.getWeekWorkTime().time.inWholeMinutes,
                todayTime = TodayTime(
                    planned = viewModel.getTodayPlannedTime().time.inWholeMinutes,
                    completed = viewModel.getTodayCompletedTime().time.inWholeMinutes
                ),
                continuesSuccessDays = ContinuesSuccessDays(
                    record = viewModel.getRecordContiniusSuccessDays().amount.toLong(),
                    now = viewModel.getTodayContinusSuccessDays().amount.toLong()
                ),
                averageDayTime = AverageDayTime(
                    totalWorkMinutes = viewModel.getTotalWorkTime().time.inWholeMinutes,
                    totalDays = 7
                )
            )
        }
    }
}

data class TotalTime(
    val common: Long,
    val work: Long,
    val study: Long,
    val fitness: Long
){
    fun toVMTotalTime(): TotalTimeTaskTypes{
        return TotalTimeTaskTypes(
            common = common,
            work = work,
            study = study,
            fitness = fitness,
        )
    }
}

data class TodayTime(
    val planned: Long,
    val completed: Long
)

data class ContinuesSuccessDays(
    val record: Long,
    val now: Long
)

data class AverageDayTime(
    val totalWorkMinutes: Long,
    val totalDays: Long
)