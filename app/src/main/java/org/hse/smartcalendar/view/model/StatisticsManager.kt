package org.hse.smartcalendar.view.model

import org.hse.smartcalendar.data.DailyTask

/**
 * Класс, передающий изменения ListVM и TaskEditVM в StatisticsVM
 */
class StatisticsManager(private val viewModel: StatisticsViewModel){
    fun changeTaskCompletion(task: DailyTask, isCompleted: Boolean){
        viewModel.changeTaskCompletion(task, isCompleted)
    }
    fun removeDailyTask(task: DailyTask, dailyTaskList: List<DailyTask>){
        viewModel.createOrDeleteTask(dailyTaskList = dailyTaskList,
            task = task,
            isCreate = false)
    }
    fun addDailyTask(task: DailyTask, dailyTaskList: List<DailyTask>){
        viewModel.createOrDeleteTask(
            dailyTaskList= dailyTaskList,
        task = task,
        isCreate = true)
    }

    /**
     * TaskEditVM->editHandler(...updateDailyTask)
    */
    fun updateDailyTask(oldTask: DailyTask, newTask: DailyTask){
        viewModel.changeTask(oldTask, newTask)
    }
}