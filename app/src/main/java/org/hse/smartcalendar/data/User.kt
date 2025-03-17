package org.hse.smartcalendar.data

class User
    (private val id: Long) {
    private val name : String
    private val schedule: MainSchedule

    init {
        // TODO server request
        name = "User"
        // TODO server request
        schedule = MainSchedule()
    }

    fun getName() : String {
        return name
    }

    fun getId() : Long {
        return id
    }

    fun getSchedule(): MainSchedule {
        return schedule
    }
}