package org.hse.smartcalendar.data

import org.hse.smartcalendar.view.model.state.TimePeriod
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
    fun getPercentByType(type: DailyTaskType): Float {
        return when (type) {
            DailyTaskType.COMMON -> CommonPercent
            DailyTaskType.FITNESS -> FitnessPercent
            DailyTaskType.WORK -> WorkPercent
            DailyTaskType.STUDIES -> StudyPercent
        }
    }
    fun completeTask(task: DailyTask, isComplete: Boolean){
        val taskMinutesLength = task.getMinutesLength().toLong()
        when(isComplete){
            true -> {
                totalMinutes+=taskMinutesLength
            }
            false -> totalMinutes-=taskMinutesLength
        }
        All.addMinutes(taskMinutesLength, isComplete)
        when (task.getDailyTaskType()) {
            DailyTaskType.COMMON -> Common.addMinutes(taskMinutesLength, isComplete)
            DailyTaskType.FITNESS -> Fitness.addMinutes(taskMinutesLength, isComplete)
            DailyTaskType.WORK -> Work.addMinutes(taskMinutesLength, isComplete)
            DailyTaskType.STUDIES -> Study.addMinutes(taskMinutesLength, isComplete)
        }
        recalculatePercents()
    }
    private fun recalculatePercents() {
        StudyPercent   = getPercent(Study.toMinutes(), totalMinutes)
        CommonPercent  = getPercent(Common.toMinutes(), totalMinutes)
        FitnessPercent = getPercent(Fitness.toMinutes(), totalMinutes)
        WorkPercent    = getPercent(Work.toMinutes(), totalMinutes)
    }
}