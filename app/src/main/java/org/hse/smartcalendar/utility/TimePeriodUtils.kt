package org.hse.smartcalendar.utility

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toDateTimePeriod
import kotlinx.datetime.toLocalDateTime
import org.hse.smartcalendar.utility.TimeUtils.Companion.numberToWord
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TimeUtils {

    companion object{
        fun getCurrentDateTime(): LocalDateTime {
            return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault());
        }
        fun numberToWord(amount: Int, item: String): String {
        if (amount != 0) {
            return if (amount > 1) "$amount $item" + "s " else "$amount $item "
        }
        return ""
    }
}
}
//every class in Kotlin is final by default, so inheritance from LocalTime not ok
class TimePeriod(minute: Long){
    private var time: Duration = Duration.ZERO
    init{
        fromMinutes(minute)
    }

    fun fromMinutes(minute: Long){
        time+=minute.minutes
    }
    fun toMinutes(): Long{
        return time.inWholeMinutes
    }
    fun plusMinutes(minutes: Long){
        time+=minutes.toDuration(DurationUnit.MINUTES)
    }
    fun toPrettyString(): String{
        val stringBuilder = StringBuilder()
        val dataTime = time.toDateTimePeriod()
        stringBuilder.append(numberToWord(dataTime.years, "year"))
        stringBuilder.append(numberToWord(dataTime.days, "day"))
        stringBuilder.append(numberToWord(dataTime.hours, "hour"))
        stringBuilder.append(numberToWord(dataTime.minutes, "minute"))
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
    fun plusMinutes(minute: Int){
        time+=minute.toDuration(DurationUnit.MINUTES);
    }
    fun toFullString(): String{
        var stringBuilder:StringBuilder = StringBuilder()
        stringBuilder.append(numberToWord(time.toDateTimePeriod().hours, "hour"))
        stringBuilder.append(numberToWord(time.toDateTimePeriod().minutes, "minute"))
        return if (stringBuilder.toString()!="") stringBuilder.toString() else "0 minute"
    }
}
