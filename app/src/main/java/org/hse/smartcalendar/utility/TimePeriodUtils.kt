package org.hse.smartcalendar.utility

import androidx.compose.runtime.mutableStateOf
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
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

    companion object {
        fun getCurrentDateTime(): LocalDateTime {
            return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault());
        }

        fun numberToWord(amount: Int, item: String): String {
            if (amount != 0) {
                return if (amount > 1) "$amount $item" + "s " else "$amount $item "
            }
            return ""
        }
        fun addDaysToNowDate(days: Int): LocalDate{
            return addDaysToDate(getCurrentDateTime().date, days)
        }
        fun addDaysToDate(date: LocalDate, days: Int): LocalDate{
            return LocalDate.fromEpochDays(date.toEpochDays()+days)
        }
        fun calcDaysDiff(reduced: LocalDate, deductible: LocalDate): Int{
            return reduced.toEpochDays()-deductible.toEpochDays()
        }
    }
}

class TimePeriod(minute: Long) {
    private var _time = mutableStateOf(Duration.ZERO)
    val time: Duration get() = _time.value

    init {
        fromMinutes(minute)
    }

    fun fromMinutes(minute: Long) {
        _time.value += minute.minutes
    }

    fun toMinutes(): Long {
        return time.inWholeMinutes
    }

    fun addMinutes(minutes: Long, sign: Boolean) {
        when (sign) {
            true -> _time.value += minutes.toDuration(DurationUnit.MINUTES)
            false -> _time.value -= minutes.toDuration(DurationUnit.MINUTES)
        }
    }

    fun toPrettyString(): String {
        val stringBuilder = StringBuilder()
        val dataTime = time.toDateTimePeriod()
        stringBuilder.append(numberToWord(dataTime.years, "year"))
        stringBuilder.append(numberToWord(dataTime.days, "day"))
        stringBuilder.append(numberToWord(dataTime.hours, "hour"))
        stringBuilder.append(numberToWord(dataTime.minutes, "minute"))
        return if (stringBuilder.toString() != "") stringBuilder.toString() else "0 minute"
    }
}

data class DaysAmount(val amount: Int) {
    fun toPrettyString(): String {
        return if (amount != 1) "$amount days" else "1 day"
    }
}

class DayPeriod(minute: Long) {
    private var _time = mutableStateOf(Duration.ZERO)
    val time: Duration get() = _time.value
    fun toMinutes(): Long {
        return time.inWholeMinutes
    }

    init {
        fromMinutes(minute)
    }

    fun fromMinutes(minute: Long) {
        _time.value = minute.toDuration(DurationUnit.MINUTES)
    }

    fun plusMinutes(minute: Int, sign: Boolean) {
        when (sign) {
            true -> _time.value += minute.toDuration(DurationUnit.MINUTES);
            false -> _time.value -= minute.toDuration(DurationUnit.MINUTES);
        }
    }

    fun toFullString(): String {
        var stringBuilder: StringBuilder = StringBuilder()
        stringBuilder.append(numberToWord(time.toDateTimePeriod().hours, "hour"))
        stringBuilder.append(numberToWord(time.toDateTimePeriod().minutes, "minute"))
        return if (stringBuilder.toString() != "") stringBuilder.toString() else "0 minute"
    }
}
