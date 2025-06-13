package org.hse.smartcalendar.view.model.state

import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import org.hse.smartcalendar.data.TotalTimeTaskTypes
import org.hse.smartcalendar.network.AverageDayTime
import org.hse.smartcalendar.network.TodayTime
import org.hse.smartcalendar.utility.TimeUtils

data class StatisticsUiState(
    val total: TotalTimeTaskTypes = TotalTimeTaskTypes(320, 180, 60, 90),
    val week: WeekTime = WeekTime(650),
    val averageDay: AverageDayTimeVars = AverageDayTimeVars(firstDay = LocalDate(2025, 6, 1), totalWorkMinutes = 870),
    val today: TodayTimeVars = TodayTimeVars(100, 60),
    val calculable: StatisticsCalculableData = StatisticsCalculableData(3, 4, 5)
)

class TodayTimeVars(planned: Long, completed: Long){
    val Planned: DayPeriod = DayPeriod(planned)
    var Completed: DayPeriod = DayPeriod(completed)
    companion object{
        fun fromTodayTimeDTO(todayTimeDTO: TodayTime): TodayTimeVars{
            return TodayTimeVars(planned = todayTimeDTO.planned,
                completed = todayTimeDTO.completed)
        }
    }
}
class AverageDayTimeVars(totalWorkMinutes: Long, val firstDay: LocalDate){
    private val dayLength = firstDay.daysUntil(TimeUtils.getCurrentDateTime().date)+1
    var All: DayPeriod = DayPeriod(totalWorkMinutes/dayLength)
    fun update(totalTimeMinutes: Long){
        All = DayPeriod(totalTimeMinutes/dayLength)
    }
    companion object{
        fun fromAverageDayDTO(averageDayTimeDTO: AverageDayTime): AverageDayTimeVars{
            return AverageDayTimeVars(totalWorkMinutes = averageDayTimeDTO.totalWorkMinutes,
                firstDay = averageDayTimeDTO.firstDay
            )
        }
    }
}
class WeekTime(all: Long){
    val All: TimePeriod = TimePeriod(all)
}