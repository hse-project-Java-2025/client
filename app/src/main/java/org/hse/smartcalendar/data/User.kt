
package org.hse.smartcalendar.data

import kotlinx.datetime.LocalDate

object User {
    private var _id: Long? = null
    private var _name: String = "User"
    private var _email: String = "User"

    val id: Long? get() = _id
    val name: String get() = _name
    val email: String get() = _email

    private val schedule = MainSchedule()
    fun getSchedule(): MainSchedule{
        return schedule
    }

    internal fun set(id: Long, name: String, email: String) {
        _id = id
        _name = name
        _email = email
    }
    fun clearSchedule(){
        schedule.initMap(HashMap<LocalDate, DailySchedule>())
    }
}