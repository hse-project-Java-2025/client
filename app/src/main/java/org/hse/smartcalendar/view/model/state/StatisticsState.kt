package org.hse.smartcalendar.view.model.state

import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import org.hse.smartcalendar.network.AverageDayTime
import org.hse.smartcalendar.network.TodayTime
import org.hse.smartcalendar.utility.TimeUtils

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