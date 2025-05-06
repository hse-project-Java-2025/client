package org.hse.smartcalendar.view.model

import androidx.lifecycle.ViewModel
import org.hse.smartcalendar.utility.DayPeriod
import org.hse.smartcalendar.utility.DaysAmount
import org.hse.smartcalendar.utility.TimePeriod

class StatisticsViewModel:ViewModel() {
    companion object {
        private fun getPercent(part: Long, all: Long): Float {
            return Math.round(part.toFloat() / all * 1000).toFloat() / 10
        }
        fun toPercent(part: Float):Float{
            return Math.round(part*1000).toFloat()/10
        }
    }
    class TotalTimeTaskTypes(val common: Long, val work: Long, val study: Long, val fitness: Long){
        val All: TimePeriod = TimePeriod(work+study+common+fitness)
        val Study: TimePeriod = TimePeriod(study)
        val Common: TimePeriod = TimePeriod(common)
        val Fitness: TimePeriod = TimePeriod(fitness)
        val Work: TimePeriod = TimePeriod(work)
        private val totalMinutes = common+study+work+fitness
        val StudyPercent: Float = getPercent(study, totalMinutes)
        val CommonPercent: Float = getPercent(common, totalMinutes)
        val FitnessPercent: Float = getPercent(fitness, totalMinutes)
        val WorkPercent: Float = getPercent(work, totalMinutes)
    }
    private class TodayTimeVars(val planned: Long, val completed: Long){
        val Planned: DayPeriod = DayPeriod(planned)
        val Completed: DayPeriod = DayPeriod(completed)
    }
    private class ContiniusSuccessDaysVars(val record: Int, val now: Int){
        val Record: DaysAmount = DaysAmount(record)
        val Now: DaysAmount = DaysAmount(now)
    }
    private class AverageDayTimeVars(val totalWorkMinutes: Long, val totalDays: Long){
        val All: DayPeriod = DayPeriod(totalWorkMinutes/totalDays)
    }
    private var ContiniusSuccessDays: ContiniusSuccessDaysVars
    private var TotalTime: TotalTimeTaskTypes = TotalTimeTaskTypes(10000, 1000, 4000, 4000)
    private val AverageDayTime: AverageDayTimeVars
    private var TodayTime: TodayTimeVars = TodayTimeVars(228, 60)
    //TODO Fitness/Common/Study CircularProgressBar
    init{
        ContiniusSuccessDays = ContiniusSuccessDaysVars(6, 5)
        AverageDayTime = AverageDayTimeVars(10000, 365)
    }
    fun update(){
        TotalTime = TotalTimeTaskTypes(60, 20, 60, 229)
        //TODO: connect to server
    }

    fun getTotalWorkTime():TimePeriod{
        return TotalTime.All
    }

    fun getTodayPlannedTime(): DayPeriod{
        return TodayTime.Planned
    }
    fun getAverageDailyTime():DayPeriod{
        return AverageDayTime.All
    }

    fun getTodayCompletedTime():DayPeriod{
        return TodayTime.Completed
    }

    fun getRecordContiniusSuccessDays():DaysAmount{
        return ContiniusSuccessDays.Record
    }
    fun getTodayContinusSuccessDays():DaysAmount{
        return ContiniusSuccessDays.Now
    }
    fun getTotalTimeActivityTypes():TotalTimeTaskTypes{
        return TotalTimeTaskTypes(TotalTime.common, TotalTime.work, TotalTime.study, TotalTime.fitness)
    }

}