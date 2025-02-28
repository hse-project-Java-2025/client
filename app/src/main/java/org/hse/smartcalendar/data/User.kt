package org.hse.smartcalendar.data

data class User (
    private val id : Long,
    private val name : String
) {
    fun getName() : String {
        return name
    }

    fun getId() : Long {
        return id
    }
}