package com.nai.routinetracker.data.local

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nai.routinetracker.model.RoutineRecurrence
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class RoutineTrackerDatabaseMigrationTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val databaseName = "routine-migration-test.db"

    @After
    fun tearDown() {
        context.deleteDatabase(databaseName)
    }

    @Test
    fun migration1To2_backfillsDailyRecurrence() = runBlocking {
        createVersionOneDatabase()

        val database = Room.databaseBuilder(
            context,
            RoutineTrackerDatabase::class.java,
            databaseName
        )
            .addMigrations(RoutineTrackerDatabase.MIGRATION_1_2)
            .build()

        val routine = database.routineDao()
            .observeRoutines()
            .first()
            .single()
            .toDomain()

        assertEquals(RoutineRecurrence.Daily, routine.recurrence)
        database.close()
    }

    private fun createVersionOneDatabase() {
        val db = SQLiteDatabase.openOrCreateDatabase(context.getDatabasePath(databaseName), null)
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS routines (
                id TEXT NOT NULL PRIMARY KEY,
                title TEXT NOT NULL,
                schedule_label TEXT NOT NULL,
                category_id TEXT NOT NULL,
                category_label TEXT NOT NULL,
                category_is_system INTEGER NOT NULL,
                streak_days INTEGER NOT NULL,
                is_active INTEGER NOT NULL,
                description TEXT NOT NULL,
                sort_order INTEGER NOT NULL
            )
            """.trimIndent()
        )
        db.execSQL(
            """
            CREATE TABLE IF NOT EXISTS tasks (
                id TEXT NOT NULL PRIMARY KEY,
                routine_id TEXT,
                title TEXT NOT NULL,
                time_label TEXT NOT NULL,
                due_label TEXT NOT NULL,
                category_id TEXT NOT NULL,
                category_label TEXT NOT NULL,
                category_is_system INTEGER NOT NULL,
                status TEXT NOT NULL,
                description TEXT NOT NULL,
                sort_order INTEGER NOT NULL,
                FOREIGN KEY(routine_id) REFERENCES routines(id) ON UPDATE NO ACTION ON DELETE SET NULL
            )
            """.trimIndent()
        )
        db.execSQL("CREATE INDEX IF NOT EXISTS index_tasks_routine_id ON tasks(routine_id)")
        db.execSQL(
            """
            INSERT INTO routines (
                id,
                title,
                schedule_label,
                category_id,
                category_label,
                category_is_system,
                streak_days,
                is_active,
                description,
                sort_order
            ) VALUES (
                'hydration',
                'Hydrate',
                '06:30 AM',
                'health',
                'Health',
                1,
                3,
                1,
                'Drink water',
                0
            )
            """.trimIndent()
        )
        db.version = 1
        db.close()
    }
}
