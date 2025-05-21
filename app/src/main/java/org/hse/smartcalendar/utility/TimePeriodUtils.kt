package org.hse.smartcalendar.utility

import kotlinx.datetime.DateTimePeriod
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toDateTimePeriod
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.toDuration

fun numberToWord(amount: Int, item: String): String {
    if (amount!=0) {
        return if (amount > 1) "$amount $item"+"s " else "$amount $item "
    }
    return ""
}
//every class in Kotlin is final by default, so inheritance from LocalTime not ok
class TimePeriod(minute: Long){
    private var time: DateTimePeriod = DateTimePeriod()
    init{
        fromMinutes(minute)
    }

    fun fromMinutes(minute: Long){
        time = (minute).toDuration(DurationUnit.MINUTES).toDateTimePeriod()
    }

    fun toPrettyString(): String{
        var stringBuilder = StringBuilder()
        stringBuilder.append(numberToWord(time.years, "year"))
        stringBuilder.append(numberToWord(time.days, "day"))
        stringBuilder.append(numberToWord(time.hours, "hour"))
        stringBuilder.append(numberToWord(time.minutes, "minute"))
        return if (stringBuilder.toString()!="") stringBuilder.toString() else "0 minute"
    }
}
class DaysAmount(amount: Int){
    private var amount: Int = amount
    fun toPrettyString():String{
        return if (amount !=1) "$amount days" else "1 day"
    }
    fun getAmount(): Int{
        return amount
    }
}
class DayPeriod(minute: Long){
    private var time: Duration = Duration.ZERO
    fun toMinutes(): Long{
        return time.inWholeMinutes
    }
    init{
        fromMinutes(minute)
    }
    fun fromMinutes(minute: Long){
        time = (minute).toDuration(DurationUnit.MINUTES)
    }
    fun toFullString(): String{
        var stringBuilder:StringBuilder = StringBuilder()
        stringBuilder.append(numberToWord(time.toDateTimePeriod().hours, "hour"))
        stringBuilder.append(numberToWord(time.toDateTimePeriod().minutes, "minute"))
        return if (stringBuilder.toString()!="") stringBuilder.toString() else "0 minute"
    }
}
