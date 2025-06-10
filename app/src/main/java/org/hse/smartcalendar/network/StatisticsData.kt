package org.hse.smartcalendar.network

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import org.hse.smartcalendar.data.TotalTimeTaskTypes
import org.hse.smartcalendar.store.StatisticsStore

@Serializable
data class StatisticsDTO(
    val totalTime: TotalTime,
    val weekTime: Long,
    val todayTime: TodayTime,
    val continuesSuccessDays: ContinuesSuccessDays,
    val averageDayTime: AverageDayTime
) {
    companion object {
        fun fromStore(): StatisticsDTO {
            val totalTime: TotalTimeTaskTypes = StatisticsStore.totalTime
            return StatisticsDTO(
                totalTime = TotalTime(
                    common = totalTime.Common.time.inWholeMinutes,
                    work = totalTime.Work.time.inWholeMinutes,
                    study = totalTime.Study.time.inWholeMinutes,
                    fitness = totalTime.Fitness.time.inWholeMinutes
                ),
                weekTime = StatisticsStore.weekTime.All.time.inWholeMinutes,
                todayTime = TodayTime(
                    planned   = StatisticsStore.todayTime.Planned.time.inWholeMinutes,
                    completed = StatisticsStore.todayTime.Completed.time.inWholeMinutes
                ),
                continuesSuccessDays = ContinuesSuccessDays(
                    record = StatisticsStore.calculator.getRecordContinuesSuccessDays().toLong(),
                    now    = StatisticsStore.calculator.getTodayContinuesSuccessDays().toLong()
                ),
                averageDayTime = AverageDayTime(
                    totalWorkMinutes = StatisticsStore.averageDayTime.All.time.inWholeMinutes,
                    firstDay         = StatisticsStore.averageDayTime.firstDay
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
    val firstDay: LocalDate
)