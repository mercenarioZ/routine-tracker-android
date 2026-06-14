package com.nai.routinetracker.data.repository

import androidx.room.withTransaction
import com.nai.routinetracker.data.local.LocalDatabaseSeeder
import com.nai.routinetracker.data.local.RoutineDao
import com.nai.routinetracker.data.local.RoutineTrackerDatabase
import com.nai.routinetracker.data.local.TaskDao
import com.nai.routinetracker.data.local.toDomain
import com.nai.routinetracker.data.local.toLocalEntity
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.model.RoutineCategory
import com.nai.routinetracker.model.RoutineDashboardState
import com.nai.routinetracker.model.RoutineItem
import com.nai.routinetracker.model.RoutineRecurrence
import com.nai.routinetracker.model.TaskItem
import com.nai.routinetracker.model.TaskStatus
import com.nai.routinetracker.model.toTaskCategory
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocalRoutineRepository @Inject constructor(
    private val database: RoutineTrackerDatabase,
    private val routineDao: RoutineDao,
    private val taskDao: TaskDao,
    private val seeder: LocalDatabaseSeeder,
    seedStringProvider: FakeSeedStringProvider
) : RoutineRepository {
    private val seedStrings = seedStringProvider.strings()

    override fun observeDashboard(): Flow<RoutineDashboardState> {
        return flow {
            seeder.seedIfNeeded()
            emitAll(
                routineDao.observeRoutines().map { routines ->
                    RoutineDashboardState(
                        userName = seedStrings.sampleUserName,
                        dateLabel = currentDateLabel(),
                        highlight = seedStrings.homeHighlight,
                        routines = routines.map { it.toDomain() }
                    )
                }
            )
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun createRoutine(
        title: String,
        scheduleLabel: String,
        category: RoutineCategory,
        recurrence: RoutineRecurrence,
        description: String
    ) {
        withContext(Dispatchers.IO) {
            seeder.seedIfNeeded()
            database.withTransaction {
                val routine = RoutineItem(
                    id = UUID.randomUUID().toString(),
                    title = title,
                    scheduleLabel = scheduleLabel,
                    category = category,
                    recurrence = recurrence,
                    streakDays = 0,
                    description = description
                )
                val task = TaskItem(
                    id = "task-${routine.id}-today",
                    routineId = routine.id,
                    title = routine.title,
                    timeLabel = routine.scheduleLabel,
                    dueLabel = seedStrings.taskTodayDueLabel,
                    category = routine.category.toTaskCategory(),
                    status = TaskStatus.Pending,
                    description = routine.description
                )

                routineDao.insert(
                    routine.toLocalEntity(sortOrder = routineDao.count())
                )
                taskDao.insert(
                    task.toLocalEntity(sortOrder = taskDao.count())
                )
            }
        }
    }

    private fun currentDateLabel(): String {
        val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.getDefault())
        return formatter.format(Date())
    }
}
