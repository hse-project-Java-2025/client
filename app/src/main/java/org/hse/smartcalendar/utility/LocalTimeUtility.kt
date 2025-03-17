package org.hse.smartcalendar.utility

import kotlinx.datetime.LocalTime


// TODO Написать нексолько утилитарных функций для работы с минутами.
fun LocalTime.Companion.fromMinutesOfDay(minutes: Int): LocalTime {
    return fromSecondOfDay(minutes * 60)
}

fun LocalTime.Companion.toMinutesOfDay(time: LocalTime): Int {
    return time.hour * 60 + time.minute
}