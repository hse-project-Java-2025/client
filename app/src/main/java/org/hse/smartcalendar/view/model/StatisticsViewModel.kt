package org.hse.smartcalendar.view.model

import androidx.lifecycle.ViewModel
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.DailyTaskType
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
    class TotalTimeTaskTypes(common: Long, work: Long, study: Long, fitness: Long){
        val All: TimePeriod = TimePeriod(work+study+common+fitness)
        val Study: TimePeriod = TimePeriod(study)
        val Common: TimePeriod = TimePeriod(common)
        val Fitness: TimePeriod = TimePeriod(fitness)
        val Work: TimePeriod = TimePeriod(work)
        private var totalMinutes = common+study+work+fitness
        var StudyPercent: Float = getPercent(study, totalMinutes)
            private  set
        var CommonPercent: Float = getPercent(common, totalMinutes)
            private  set
        var FitnessPercent: Float = getPercent(fitness, totalMinutes)
            private  set
        var WorkPercent: Float = getPercent(work, totalMinutes)
            private  set
        fun completeTask(task: DailyTask){
            val taskMinutesLength = task.getMinutesLength()
            totalMinutes+=taskMinutesLength
            when(task.getDailyTaskType()){
                DailyTaskType.COMMON -> {Common.plusMinutes(taskMinutesLength.toLong())
                CommonPercent=getPercent(Common.toMinutes(), totalMinutes)}
                DailyTaskType.FITNESS -> {
                    Fitness.plusMinutes(taskMinutesLength.toLong())
                    FitnessPercent=getPercent(Fitness.toMinutes(), totalMinutes)
                }
                DailyTaskType.WORK -> {
                    Work.plusMinutes(taskMinutesLength.toLong())
                    WorkPercent=getPercent(Work.toMinutes(), totalMinutes)
                }
                DailyTaskType.STUDIES -> {
                    Study.plusMinutes(taskMinutesLength.toLong())
                    StudyPercent=getPercent(Study.toMinutes(), totalMinutes)
                }
            }
        }
    }
    class WeekTime(all: Long){
        val All: TimePeriod = TimePeriod(all)
    }
    private class TodayTimeVars(planned: Long, completed: Long){
        val Planned: DayPeriod = DayPeriod(planned)
        var Completed: DayPeriod = DayPeriod(completed)
    }
    private class ContinuesSuccessDaysVars(record: Int, now: Int){
        val Record: DaysAmount = DaysAmount(record)
        val Now: DaysAmount = DaysAmount(now)
    }
    private class AverageDayTimeVars(totalWorkMinutes: Long, totalDays: Long){
        val All: DayPeriod = DayPeriod(totalWorkMinutes/totalDays)
    }
    private var ContiniusSuccessDays: ContinuesSuccessDaysVars
    private var TotalTime: TotalTimeTaskTypes = TotalTimeTaskTypes(10000, 1000, 4000, 4000)
    private var weekTime = WeekTime(60*60)
    private val AverageDayTime: AverageDayTimeVars
    private var TodayTime: TodayTimeVars = TodayTimeVars(228, 60)
    init{
        ContiniusSuccessDays = ContinuesSuccessDaysVars(6, 5)
        AverageDayTime = AverageDayTimeVars(10000, 365)
    }
    fun createTask(task: DailyTask){
        if (task.belongsCurrentDay()){
            TodayTime.Planned.plusMinutes(task.getMinutesLength())
        }
    }
    fun completeTask(task: DailyTask){//когда таска запатчена
        val taskMinutesLength = task.getMinutesLength()
        if (task.belongsCurrentDay()) {
            TodayTime.Completed.plusMinutes(taskMinutesLength)
        }
        if (task.belongsCurrentWeek()){
            weekTime.All.plusMinutes(taskMinutesLength.toLong())
        }
        TotalTime.completeTask(task)
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
    fun getWeekWorkTime(): TimePeriod{
        return weekTime.All
    }
    fun getTypesInDay(): Long{
        return 2
    }
}