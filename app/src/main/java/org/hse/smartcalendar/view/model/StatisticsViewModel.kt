package org.hse.smartcalendar.view.model

import androidx.lifecycle.ViewModel
import org.hse.smartcalendar.utility.DayPeriod
import org.hse.smartcalendar.utility.DaysAmount
import org.hse.smartcalendar.utility.TimePeriod

class StatisticsViewModel:ViewModel() {
    private var TotalWorkTime: TimePeriod = TimePeriod(1000000)//In minutes
    private var TotalDays:DaysAmount = DaysAmount(365)
    private var DailyWorkTime: DayPeriod = DayPeriod(555)
    private var TodayWorkTime: DayPeriod = DayPeriod(228)
    private var TodayCompletedTime:DayPeriod = DayPeriod(60)
    private var RecordContiniusSuccessDays: DaysAmount
    private var TodayContinusSuccessDays: DaysAmount
    //TODO Fitness/Common/Study CircularProgressBar
    init{
        RecordContiniusSuccessDays = DaysAmount(6)
        TodayContinusSuccessDays = DaysAmount(5)
    }
    fun update(){
        //TODO: connect to server
    }

    fun getTotalWorkTime():TimePeriod{
        return TotalWorkTime
    }

    fun getTotalDays(): DaysAmount{
        return TotalDays
    }
    fun getTodayWorkTime(): DayPeriod{
        return TodayWorkTime
    }
    fun getDailyWorkTime():DayPeriod{
        return DailyWorkTime
    }

    fun getTodayCompletedTime():DayPeriod{
        return TodayCompletedTime
    }

    fun getRecordContiniusSuccessDays():DaysAmount{
        return RecordContiniusSuccessDays
    }
    fun getTodayContinusSuccessDays():DaysAmount{
        return TodayContinusSuccessDays
    }
}