package org.hse.smartcalendar.data

import kotlinx.datetime.LocalDate

class MainSchedule {
    private var dailyScheduleMap = mutableMapOf<LocalDate, DailySchedule>()
    fun initMap(m: MutableMap<LocalDate, DailySchedule> ){
        dailyScheduleMap = m
    }
    //инициализировать нужно
    fun containsDailySchedule(date: LocalDate): Boolean {
        return dailyScheduleMap.contains(date)
    }

    fun getOrCreateDailySchedule(date: LocalDate): DailySchedule {
        return dailyScheduleMap.getOrPut(date) { DailySchedule(date) }
    }
}