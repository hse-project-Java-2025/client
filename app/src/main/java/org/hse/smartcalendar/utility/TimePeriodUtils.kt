package org.hse.smartcalendar.utility

fun numberToWord(amount: Int, item: String): String {
    if (amount!=0) {
        return if (amount > 1) "$amount $item"+"s " else "$amount $item "
    }
    return ""
}
//every class in Kotlin is final by default, so inheritance from LocalTime not ok
class TimePeriod(minute: Long){
    private var year: Int= (minute/525967).toInt()
    private var days: Int = (minute%525967/60/24).toInt()
    private var hours: Int = (minute%525967%60/24).toInt()
    private var minutes: Int = (minute%525967%60%24).toInt()
    fun toMinutes(): Long{
        return (minutes).toLong()+60*hours+days*60*24+year*60*24*525967
    }

    fun toPrettyString(): String{
        var stringBuilder = StringBuilder()
        stringBuilder.append(numberToWord(year, "year"))
        stringBuilder.append(numberToWord(days, "day"))
        stringBuilder.append(numberToWord(hours, "hour"))
        stringBuilder.append(numberToWord(minutes, "minute"))
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
    private var hours: Int = (minute/60).toInt()
    private var minutes: Int = (minute%60).toInt()
    fun toMinutes(): Long{
        return (minutes).toLong()+60*hours
    }
    fun toFullString(): String{
        var stringBuilder:StringBuilder = StringBuilder()
        stringBuilder.append(numberToWord(hours, "hour"))
        stringBuilder.append(numberToWord(minutes, "minute"))
        return if (stringBuilder.toString()!="") stringBuilder.toString() else "0 minute"
    }
}