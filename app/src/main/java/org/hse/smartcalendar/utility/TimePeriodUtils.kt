package org.hse.smartcalendar.utility

import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

class TimeUtils {

    companion object {
        fun getCurrentDateTime(): LocalDateTime {
            return Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault());
        }
        fun getCurrentTimezone(): TimeZone{
            return TimeZone.currentSystemDefault()
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

