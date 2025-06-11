package org.hse.smartcalendar.view.model.state

import androidx.compose.runtime.mutableStateOf
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.daysUntil
import kotlinx.datetime.minus
import org.hse.smartcalendar.data.DailyTask
import org.hse.smartcalendar.data.User
import androidx.compose.runtime.State
import org.hse.smartcalendar.network.StatisticsDTO
import org.hse.smartcalendar.utility.TimeUtils
data class StatisticsCalculableData(
    val typesInDay: Int,
    val continuesCurrent: Int,
    val continuesTotal: Int
)
class StatisticsCalculator {
    private val _stats = mutableStateOf(StatisticsCalculableData(0,0,0))
    val stats: State<StatisticsCalculableData> = _stats
    private fun recalculateTypes(dayTasks:  List<DailyTask> ){
        val state = stats.value
        val dayTypes = dayTasks
            .map { it.getDailyTaskType() }
            .toSet()
            .size
        _stats.value = StatisticsCalculableData(dayTypes,
            continuesTotal = state.continuesTotal,
            continuesCurrent = state.continuesCurrent)
    }
    private fun recalculateDays(){
        val currentDate = TimeUtils.Companion.getCurrentDateTime().date
        var lastSuccessDate = currentDate
        val mainSchedule = User.getSchedule()
        val isSuccess: (LocalDate)->Boolean = { date ->
            val tasks = mainSchedule
                .getOrCreateDailySchedule(date)
                .getDailyTaskList()
                .map { it.isComplete() }
            !tasks.isEmpty()&&tasks.all{it}
        }
        while (isSuccess(lastSuccessDate)){
            lastSuccessDate = lastSuccessDate.minus(1, DateTimeUnit.DAY)
        }
        val continuesCurrent = lastSuccessDate.daysUntil(currentDate)
        val continuesTotal = maxOf(stats.value.continuesTotal, continuesCurrent)
        _stats.value = StatisticsCalculableData(
            typesInDay = stats.value.typesInDay,
            continuesTotal = continuesTotal,
            continuesCurrent = continuesCurrent
            )
    }
    private fun recalculateDays(changedDate: LocalDate){
        val currentDate = TimeUtils.Companion.getCurrentDateTime().date
        if (changedDate>currentDate
            || changedDate.daysUntil(currentDate)>stats.value.continuesCurrent){
            return
        }
        recalculateDays()
    }
    data class AddOrDeleteRequest(val dateTasks: List<DailyTask>, val date: LocalDate)
    fun addOrDeleteTask(request: AddOrDeleteRequest){
        if (request.date== TimeUtils.getCurrentDateTime().date) {
            recalculateTypes(request.dateTasks)
        }
        recalculateDays(request.date)
    }
    fun changeTaskCompletion(task: DailyTask){
        recalculateDays(task.getTaskDate())
    }
    fun init(statisticsDTO: StatisticsDTO){
        _stats.value = StatisticsCalculableData(typesInDay = 0,
            continuesTotal = statisticsDTO.continuesSuccessDays.record.toInt(),
            continuesCurrent = 0)
        recalculateDays()
        val currentDate = TimeUtils.Companion.getCurrentDateTime().date
        val tasks = User.getSchedule().getOrCreateDailySchedule(currentDate)
            .getDailyTaskList()
        recalculateTypes(tasks)
    }
    fun getCurrentDayTypes(): Int{
        return stats.value.typesInDay
    }
    fun getTodayContinuesSuccessDays(): Int{
        return stats.value.continuesCurrent
    }
    fun getRecordContinuesSuccessDays(): Int{
        return stats.value.continuesTotal
    }
}