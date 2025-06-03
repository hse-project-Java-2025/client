package org.hse.smartcalendar.database

import android.content.Context
import kotlinx.coroutines.flow.Flow

class PendingTaskActionRepository(context: Context) {
    private val dao = AppDatabase.getInstance(context).pendingTaskActionDao()

    fun getAllActionsFlow(): Flow<List<PendingTaskActionEntity>> =
        dao.getAllActionsFlow()

    suspend fun getAllActions(): List<PendingTaskActionEntity> =
        dao.getAllActions()

    suspend fun insertAction(entity: PendingTaskActionEntity): Long =
        dao.insert(entity)

    suspend fun deleteActionById(id: Long) =
        dao.deleteById(id)

    suspend fun clearAll() = dao.clearAll()
}