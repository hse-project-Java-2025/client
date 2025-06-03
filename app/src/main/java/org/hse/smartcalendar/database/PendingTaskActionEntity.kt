package org.hse.smartcalendar.database
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import kotlinx.serialization.Serializable
import org.hse.smartcalendar.data.DailyTask

@Entity(tableName = "pending_task_actions")
@TypeConverters(DatabaseTypeAdapter::class)
@Serializable
data class PendingTaskActionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val task: DailyTask,
    val actionType: String,
    val timestamp: Long
)