package org.hse.smartcalendar.data

data class SuggestedTask (
    val task: DailyTask,
    var status: TaskStatus = TaskStatus.PENDING){
    enum class TaskStatus { PENDING, ACCEPTED, DECLINED }
}