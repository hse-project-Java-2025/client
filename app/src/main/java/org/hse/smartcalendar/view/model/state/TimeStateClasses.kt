package org.hse.smartcalendar.view.model.state

import androidx.compose.runtime.mutableStateOf
import kotlinx.datetime.toDateTimePeriod
import org.hse.smartcalendar.utility.TimeUtils
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class TimePeriod(minute: Long) {
    private var _time = mutableStateOf(Duration.Companion.ZERO)
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
        stringBuilder.append(TimeUtils.Companion.numberToWord(dataTime.years, "year"))
        stringBuilder.append(TimeUtils.Companion.numberToWord(dataTime.days, "day"))
        stringBuilder.append(TimeUtils.Companion.numberToWord(dataTime.hours, "hour"))
        stringBuilder.append(TimeUtils.Companion.numberToWord(dataTime.minutes, "minute"))
        return if (stringBuilder.toString() != "") stringBuilder.toString() else "0 minute"
    }
}

data class DaysAmount(val amount: Int) {
    fun toPrettyString(): String {
        return if (amount != 1) "$amount days" else "1 day"
    }
}

class DayPeriod(minute: Long) {
    private var _time = mutableStateOf(Duration.Companion.ZERO)
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
        stringBuilder.append(
            TimeUtils.Companion.numberToWord(
                time.toDateTimePeriod().hours,
                "hour"
            )
        )
        stringBuilder.append(
            TimeUtils.Companion.numberToWord(
                time.toDateTimePeriod().minutes,
                "minute"
            )
        )
        return if (stringBuilder.toString() != "") stringBuilder.toString() else "0 minute"
    }
}