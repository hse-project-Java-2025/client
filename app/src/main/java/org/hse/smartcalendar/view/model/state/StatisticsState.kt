package org.hse.smartcalendar.view.model.state

import org.hse.smartcalendar.network.AverageDayTime
import org.hse.smartcalendar.network.TodayTime
import kotlin.math.max

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
class AverageDayTimeVars(totalWorkMinutes: Long, val totalDays: Long){
    var All: DayPeriod = DayPeriod(totalWorkMinutes/totalDays)
    fun update(totalTimeMinutes: Long){
        All = DayPeriod(totalTimeMinutes/totalDays)
    }
    companion object{
        fun fromAverageDayDTO(averageDayTimeDTO: AverageDayTime): AverageDayTimeVars{
            return AverageDayTimeVars(totalWorkMinutes = averageDayTimeDTO.totalWorkMinutes,
                totalDays = max(averageDayTimeDTO.totalDays, 1)
            )
        }
    }
}
class WeekTime(all: Long){
    val All: TimePeriod = TimePeriod(all)
}