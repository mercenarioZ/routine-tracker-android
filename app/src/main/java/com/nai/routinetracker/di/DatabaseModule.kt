package com.nai.routinetracker.di

import android.content.Context
import androidx.room.Room
import com.nai.routinetracker.data.local.RoutineDao
import com.nai.routinetracker.data.local.RoutineTrackerDatabase
import com.nai.routinetracker.data.local.TaskDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideRoutineTrackerDatabase(
        @ApplicationContext context: Context
    ): RoutineTrackerDatabase {
        return Room.databaseBuilder(
            context,
            RoutineTrackerDatabase::class.java,
            "routine_tracker.db"
        )
            .addMigrations(RoutineTrackerDatabase.MIGRATION_1_2)
            .build()
    }

    @Provides
    fun provideRoutineDao(database: RoutineTrackerDatabase): RoutineDao {
        return database.routineDao()
    }

    @Provides
    fun provideTaskDao(database: RoutineTrackerDatabase): TaskDao {
        return database.taskDao()
    }
}
