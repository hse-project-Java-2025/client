package org.hse.smartcalendar.view.model

import javax.inject.Inject
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.repository.StatisticsRepository

/**
 * Класс, передающий изменения ListVM и TaskEditVM в StatisticsVM
 */
class StatisticsManager  @Inject constructor(
    private val statisticsRepo: StatisticsRepository){
    fun changeTaskCompletion(task: DailyTask, isCompleted: Boolean){
        viewModel.changeTaskCompletion(task, isCompleted)
    }
    fun removeDailyTask(task: DailyTask){
        viewModel.createOrDeleteTask(task, false)
    }
    fun addDailyTask(task: DailyTask){
        viewModel.createOrDeleteTask(task, true)
    }

    /**
     * TaskEditVM->editHandler(...updateDailyTask)
    */
    fun updateDailyTask(oldTask: DailyTask, newTask: DailyTask){
        viewModel.changeTask(oldTask, newTask)
    }
}