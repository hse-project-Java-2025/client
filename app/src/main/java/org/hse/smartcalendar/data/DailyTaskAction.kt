package org.hse.smartcalendar.data

import kotlinx.serialization.Serializable

@Serializable
class DailyTaskAction(val type: Type, val task: DailyTask) {
    enum class Type(private val printName: String, ) {
        EDIT("Edit"),
        DELETE("Delete"),
        CHANGE_COMPLETION("Change completion"),
        ADD("Add");

        @Override
        override fun toString(): String {
            return name
        }
    }
    companion object{
        val jsonName = "task_action_json"
    }
}