package com.nai.routinetracker.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nai.routinetracker.data.local.LocalDatabaseSeeder
import com.nai.routinetracker.data.local.RoutineTrackerDatabase
import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.RoutineRecurrence
import com.nai.routinetracker.model.RoutineWeekday
import com.nai.routinetracker.model.TaskStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class LocalRepositoryPersistenceTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val seedStringProvider = LocalStaticFakeSeedStringProvider()
    private val databaseName = "local-repository-test.db"

    private lateinit var database: RoutineTrackerDatabase

    @Before
    fun setUp() {
        context.deleteDatabase(databaseName)
        database = openDatabase()
    }

    @After
    fun tearDown() {
        database.close()
        context.deleteDatabase(databaseName)
    }

    @Test
    fun createRoutine_persistsRoutineAndGeneratedTaskAfterDatabaseReopen() = runBlocking {
        val repositories = repositoriesFor(database)

        repositories.routineRepository.createRoutine(
            title = "Evening reset",
            scheduleLabel = "08:30 PM",
            category = RoutineCategories.Planning,
            recurrence = RoutineRecurrence.Custom(setOf(RoutineWeekday.Monday, RoutineWeekday.Friday)),
            description = "Tidy the desk and plan tomorrow."
        )

        database.close()
        database = openDatabase()
        val reopenedRepositories = repositoriesFor(database)

        val routines = reopenedRepositories.routineRepository
            .observeDashboard()
            .first()
            .routines
        val tasks = reopenedRepositories.taskRepository
            .observeTasks()
            .first()

        val createdRoutine = routines.single { it.title == "Evening reset" }
        val generatedTask = tasks.single { it.routineId == createdRoutine.id }

        assertEquals("08:30 PM", createdRoutine.scheduleLabel)
        assertEquals(RoutineCategories.Planning.id, createdRoutine.category.id)
        assertEquals(
            RoutineRecurrence.Custom(setOf(RoutineWeekday.Monday, RoutineWeekday.Friday)),
            createdRoutine.recurrence
        )
        assertEquals("Tidy the desk and plan tomorrow.", createdRoutine.description)
        assertEquals(TaskStatus.Pending, generatedTask.status)
        assertEquals(createdRoutine.title, generatedTask.title)
        assertEquals(createdRoutine.scheduleLabel, generatedTask.timeLabel)
        assertEquals("Today", generatedTask.dueLabel)
    }

    @Test
    fun toggleTask_persistsStatusAfterDatabaseReopen() = runBlocking {
        val repositories = repositoriesFor(database)
        val taskId = "task-hydration-today"

        assertTrue(repositories.taskRepository.observeTasks().first().any { it.id == taskId })

        repositories.taskRepository.toggleTask(taskId)

        database.close()
        database = openDatabase()
        val reopenedRepositories = repositoriesFor(database)

        val task = reopenedRepositories.taskRepository
            .observeTasks()
            .first()
            .single { it.id == taskId }

        assertEquals(TaskStatus.Done, task.status)
    }

    private fun openDatabase(): RoutineTrackerDatabase {
        return Room.databaseBuilder(
            context,
            RoutineTrackerDatabase::class.java,
            databaseName
        )
            .addMigrations(RoutineTrackerDatabase.MIGRATION_1_2)
            .build()
    }

    private fun repositoriesFor(database: RoutineTrackerDatabase): LocalRepositories {
        val seeder = LocalDatabaseSeeder(
            database = database,
            routineDao = database.routineDao(),
            taskDao = database.taskDao(),
            seedStringProvider = seedStringProvider
        )
        return LocalRepositories(
            routineRepository = LocalRoutineRepository(
                database = database,
                routineDao = database.routineDao(),
                taskDao = database.taskDao(),
                seeder = seeder,
                seedStringProvider = seedStringProvider
            ),
            taskRepository = LocalTaskRepository(
                taskDao = database.taskDao(),
                seeder = seeder,
                seedStringProvider = seedStringProvider
            )
        )
    }

    private data class LocalRepositories(
        val routineRepository: LocalRoutineRepository,
        val taskRepository: LocalTaskRepository
    )
}

private class LocalStaticFakeSeedStringProvider : FakeSeedStringProvider {
    override fun strings(): FakeSeedStrings {
        return FakeSeedStrings(
            sampleUserName = "Builder",
            homeHighlight = "Small routines create stable days.",
            hydrationTitle = "Hydrate and stretch",
            hydrationSchedule = "06:30 AM",
            hydrationDescription = "Drink water and stretch.",
            planningTitle = "Plan top 3 tasks",
            planningSchedule = "07:00 AM",
            planningDescription = "Write the three outcomes.",
            focusTitle = "Deep work block",
            focusSchedule = "09:00 AM",
            focusDescription = "Focus on the hardest task.",
            learningTitle = "Android learning sprint",
            learningSchedule = "08:30 PM",
            learningDescription = "Improve one native Android skill.",
            taskTodayDueLabel = "Today",
            taskTomorrowDueLabel = "Tomorrow"
        )
    }
}
