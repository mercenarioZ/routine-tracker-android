package com.nai.routinetracker.data.repository

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.nai.routinetracker.data.local.LocalDatabaseSeeder
import com.nai.routinetracker.data.local.RoutineTrackerDatabase
import com.nai.routinetracker.model.RoutineCategories
import com.nai.routinetracker.model.TaskStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [35])
class RoomLocalDataRepositoryTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()
    private val seedStringProvider = ResetTestFakeSeedStringProvider()
    private val databaseName = "local-data-reset-test.db"

    private lateinit var database: RoutineTrackerDatabase

    @Before
    fun setUp() {
        context.deleteDatabase(databaseName)
        database = Room.databaseBuilder(
            context,
            RoutineTrackerDatabase::class.java,
            databaseName
        ).build()
    }

    @After
    fun tearDown() {
        database.close()
        context.deleteDatabase(databaseName)
    }

    @Test
    fun resetLocalData_clearsLocalChangesAndRestoresSampleData() = runBlocking {
        val repositories = repositoriesFor(database)

        repositories.routineRepository.createRoutine(
            title = "Evening reset",
            scheduleLabel = "08:30 PM",
            category = RoutineCategories.Planning,
            description = "Tidy the desk and plan tomorrow."
        )
        repositories.taskRepository.toggleTask("task-focus-today")

        val changedRoutines = repositories.routineRepository.observeDashboard().first().routines
        val changedTasks = repositories.taskRepository.observeTasks().first()
        assertTrue(changedRoutines.any { it.title == "Evening reset" })
        assertEquals(TaskStatus.Pending, changedTasks.single { it.id == "task-focus-today" }.status)

        repositories.localDataRepository.resetLocalData()

        val resetRoutines = repositories.routineRepository.observeDashboard().first().routines
        val resetTasks = repositories.taskRepository.observeTasks().first()

        assertEquals(4, resetRoutines.size)
        assertFalse(resetRoutines.any { it.title == "Evening reset" })
        assertTrue(resetRoutines.any { it.id == "hydration" })
        assertEquals(4, resetTasks.size)
        assertEquals(TaskStatus.Done, resetTasks.single { it.id == "task-focus-today" }.status)
        assertEquals(TaskStatus.Pending, resetTasks.single { it.id == "task-hydration-today" }.status)
    }

    private fun repositoriesFor(database: RoutineTrackerDatabase): ResetTestRepositories {
        val seeder = LocalDatabaseSeeder(
            database = database,
            routineDao = database.routineDao(),
            taskDao = database.taskDao(),
            seedStringProvider = seedStringProvider
        )
        return ResetTestRepositories(
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
            ),
            localDataRepository = RoomLocalDataRepository(
                seeder = seeder
            )
        )
    }

    private data class ResetTestRepositories(
        val routineRepository: LocalRoutineRepository,
        val taskRepository: LocalTaskRepository,
        val localDataRepository: RoomLocalDataRepository
    )
}

private class ResetTestFakeSeedStringProvider : FakeSeedStringProvider {
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
