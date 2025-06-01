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
        var totalMinutes = common+study+work+fitness
            private set
        var StudyPercent: Float = getPercent(study, totalMinutes)
            private  set
        var CommonPercent: Float = getPercent(common, totalMinutes)
            private  set
        var FitnessPercent: Float = getPercent(fitness, totalMinutes)
            private  set
        var WorkPercent: Float = getPercent(work, totalMinutes)
            private  set
        fun completeTask(task: DailyTask, isComplete: Boolean){
            val taskMinutesLength = task.getMinutesLength().toLong()
            when(isComplete){
                true -> {
                    totalMinutes+=taskMinutesLength
                }
                false -> totalMinutes-=taskMinutesLength
            }
            All.addMinutes(taskMinutesLength, isComplete)
            when(task.getDailyTaskType()){
                DailyTaskType.COMMON -> {Common.addMinutes(taskMinutesLength, isComplete)
                CommonPercent=getPercent(Common.toMinutes(), totalMinutes)}
                DailyTaskType.FITNESS -> {
                    Fitness.addMinutes(taskMinutesLength, isComplete)
                    FitnessPercent=getPercent(Fitness.toMinutes(), totalMinutes)
                }
                DailyTaskType.WORK -> {
                    Work.addMinutes(taskMinutesLength, isComplete)
                    WorkPercent=getPercent(Work.toMinutes(), totalMinutes)
                }
                DailyTaskType.STUDIES -> {
                    Study.addMinutes(taskMinutesLength, isComplete)
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
    private class AverageDayTimeVars(totalWorkMinutes: Long, val totalDays: Long){
        var All: DayPeriod = DayPeriod(totalWorkMinutes/totalDays)
            private set
        fun update(totalTimeMinutes: Long){
            All = DayPeriod(totalTimeMinutes/totalDays)
        }
    }
    private val ContiniusSuccessDays: ContinuesSuccessDaysVars
    private val TotalTime: TotalTimeTaskTypes = TotalTimeTaskTypes(0, 0, 0, 0)
    private val weekTime = WeekTime(0)
    private val AverageDayTime: AverageDayTimeVars
    private val TodayTime: TodayTimeVars = TodayTimeVars(0, 0)
    init{
        ContiniusSuccessDays = ContinuesSuccessDaysVars(0, 0)
        AverageDayTime = AverageDayTimeVars(0, 1)
    }
    fun createOrDeleteTask(task: DailyTask, isCreate: Boolean){
        if (task.isComplete() && isCreate==false){
            changeTaskCompletion(task, false)
        }
        if (task.belongsCurrentDay()){
            TodayTime.Planned.plusMinutes(task.getMinutesLength(), isCreate)
        }
    }

    fun changeTaskCompletion(task: DailyTask, isComplete: Boolean){//когда таска запатчена
        val taskMinutesLength = task.getMinutesLength()
        if (task.belongsCurrentDay()) {
            TodayTime.Completed.plusMinutes(taskMinutesLength, isComplete)
        }
        if (task.belongsCurrentWeek()){
            weekTime.All.addMinutes(taskMinutesLength.toLong(), isComplete)
        }
        TotalTime.completeTask(task, isComplete)
        AverageDayTime.update(TotalTime.totalMinutes)

    }

    fun changeTask(task: DailyTask, newTask: DailyTask){
        if (task.isComplete()){
            changeTaskCompletion(task, false)
            changeTaskCompletion(newTask, true)
        }
        createOrDeleteTask(task, false)
        createOrDeleteTask(newTask, true)
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
        return TotalTimeTaskTypes(TotalTime.Common.time.inWholeMinutes, TotalTime.Work.time.inWholeMinutes, TotalTime.Study.time.inWholeMinutes, TotalTime.Fitness.time.inWholeMinutes)
    }
    fun getWeekWorkTime(): TimePeriod{
        return weekTime.All
    }
    fun getTypesInDay(): Long{
        return 2
    }
}