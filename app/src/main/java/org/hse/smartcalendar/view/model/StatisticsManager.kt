package org.hse.smartcalendar.view.model

import jakarta.inject.Inject
import org.hse.smartcalendar.data.DailyTask

/**
 * Класс, передающий изменения ListVM и TaskEditVM в StatisticsVM
 */
class StatisticsManager @Inject constructor(private val viewModel: AbstractStatisticsViewModel){
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