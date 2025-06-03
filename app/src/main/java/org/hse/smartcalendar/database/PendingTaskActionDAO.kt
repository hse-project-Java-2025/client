package org.hse.smartcalendar.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy
import kotlinx.coroutines.flow.Flow

@Dao
interface PendingTaskActionDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(action: PendingTaskActionEntity): Long

    @Query("SELECT * FROM pending_task_actions ORDER BY timestamp ASC")
    fun getAllActionsFlow(): Flow<List<PendingTaskActionEntity>>

    @Query("SELECT * FROM pending_task_actions ORDER BY timestamp ASC")
    suspend fun getAllActions(): List<PendingTaskActionEntity>

    @Query("DELETE FROM pending_task_actions WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM pending_task_actions")
    suspend fun clearAll()
}