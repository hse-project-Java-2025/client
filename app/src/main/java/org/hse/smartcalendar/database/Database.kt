package org.hse.smartcalendar.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [PendingTaskActionEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseTypeAdapter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun pendingTaskActionDao(): PendingTaskActionDAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "smart_calendar_db"
                )
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}