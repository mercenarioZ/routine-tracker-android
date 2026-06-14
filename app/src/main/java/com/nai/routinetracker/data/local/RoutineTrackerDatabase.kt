package com.nai.routinetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(
    entities = [
        LocalRoutineEntity::class,
        LocalTaskEntity::class
    ],
    version = 2,
    exportSchema = false
)
abstract class RoutineTrackerDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    abstract fun taskDao(): TaskDao

    companion object {
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE routines ADD COLUMN recurrence_type TEXT NOT NULL DEFAULT 'daily'")
                db.execSQL("ALTER TABLE routines ADD COLUMN recurrence_days TEXT NOT NULL DEFAULT ''")
            }
        }
    }
}
