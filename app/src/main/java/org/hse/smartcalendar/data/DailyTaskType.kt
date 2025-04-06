package org.hse.smartcalendar.data

enum class DailyTaskType(private val printName: String) {
    COMMON("Common"),
    FITNESS("Fitness"),
    WORK("Work"),
    STUDIES("Studies");

    @Override
    override fun toString(): String {
        return printName
    }
    companion object {
        fun fromString(type: String): DailyTaskType {
            return entries.first { it.printName == type }
        }
    }
}
