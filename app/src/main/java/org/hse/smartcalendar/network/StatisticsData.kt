package org.hse.smartcalendar.network

import kotlinx.serialization.Serializable
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
import org.hse.smartcalendar.data.TotalTimeTaskTypes
import org.hse.smartcalendar.view.model.StatisticsViewModel
@Serializable
data class StatisticsDTO(
    val totalTime: TotalTime,
    val weekTime: Long,
    val todayTime: TodayTime,
    val continuesSuccessDays: ContinuesSuccessDays,
    val averageDayTime: AverageDayTime
) {
    companion object {
        fun fromViewModel(viewModel: StatisticsViewModel): StatisticsDTO {
            val totalTime = viewModel.getTotalTimeActivityTypes()

            return StatisticsDTO(
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
@Serializable
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
    fun updated(task: DailyTask, isComplete: Boolean): TotalTime{
        return when(task.getDailyTaskType()){
            DailyTaskType.COMMON -> TotalTime(common+task.getMinutesLengthSigned(isComplete),
                work, study, fitness)
            DailyTaskType.FITNESS -> TotalTime(common,
                work+task.getMinutesLengthSigned(isComplete), study, fitness)
            DailyTaskType.WORK -> TotalTime(common,
                work, study+task.getMinutesLengthSigned(isComplete), fitness)
            DailyTaskType.STUDIES -> TotalTime(common,
                work, study, fitness+task.getMinutesLengthSigned(isComplete))
        }
    }
}
@Serializable
data class TodayTime(
    val planned: Long,
    val completed: Long
)
@Serializable
data class ContinuesSuccessDays(
    val record: Long,
    val now: Long
)
@Serializable
data class AverageDayTime(
    val totalWorkMinutes: Long,
    val totalDays: Long
){
    fun updateTime(task: DailyTask, isComplete: Boolean): AverageDayTime{
        return AverageDayTime(totalWorkMinutes+task.getMinutesLengthSigned(isComplete), totalDays)
    }
}