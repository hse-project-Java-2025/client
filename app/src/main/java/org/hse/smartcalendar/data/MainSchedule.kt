package org.hse.smartcalendar.data

import kotlinx.datetime.LocalDate

class MainSchedule {
    private val dailyScheduleMap = mutableMapOf<LocalDate, DailySchedule>()

    fun containsDailySchedule(date: LocalDate): Boolean {
        return dailyScheduleMap.contains(date)
    }

    fun getOrCreateDailySchedule(date: LocalDate): DailySchedule {
        return dailyScheduleMap.getOrPut(date) { DailySchedule(date) }
    }
}