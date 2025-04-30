package org.hse.smartcalendar.utility

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime


// TODO Написать нексолько утилитарных функций для работы с минутами.
fun LocalTime.Companion.fromMinutesOfDay(minutes: Int): LocalTime {
    return fromSecondOfDay(minutes * 60)
}

fun LocalTime.Companion.toMinutesOfDay(time: LocalTime): Int {
    return time.hour * 60 + time.minute
}
fun LocalTime.Companion.prettyPrint(time: LocalTime): String {
    return time.hour.toString()+"h"+time.minute.toString()+"m"
}
fun LocalDate.Companion.fromEpochSeconds(epochSeconds: Long): LocalDate {
    return fromEpochDays((epochSeconds/1000/60/60/24).toInt())
}
fun LocalDate.Companion.toEpochMilliseconds(localDate: LocalDate): Long {
    return localDate.toEpochDays().toLong()*1000*60*60*24
}