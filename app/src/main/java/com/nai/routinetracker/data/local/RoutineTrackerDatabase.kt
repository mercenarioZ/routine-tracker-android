package com.nai.routinetracker.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [
        LocalRoutineEntity::class,
        LocalTaskEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class RoutineTrackerDatabase : RoomDatabase() {
    abstract fun routineDao(): RoutineDao

    abstract fun taskDao(): TaskDao
}
