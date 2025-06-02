package org.hse.smartcalendar.data

import android.content.Context
import androidx.work.WorkManager

object WorkManagerHolder {
    private var workManager: WorkManager? = null

    fun init(context: Context) {
        workManager = WorkManager.getInstance(context.applicationContext)
    }

    fun getInstance(): WorkManager {
        return workManager
            ?: throw IllegalStateException("WorkManagerHolder is not initialized")
    }
    fun setManagerForTests(workManager: WorkManager){
        this.workManager = workManager
    }
}