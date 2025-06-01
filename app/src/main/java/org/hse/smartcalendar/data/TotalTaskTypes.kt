package org.hse.smartcalendar.data

import org.hse.smartcalendar.utility.TimePeriod
import org.hse.smartcalendar.view.model.StatisticsViewModel.Companion.getPercent

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