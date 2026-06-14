package com.nai.routinetracker.data.local

import androidx.room.withTransaction
import com.nai.routinetracker.data.repository.FakeSeedStringProvider
import com.nai.routinetracker.data.repository.buildSampleRoutines
import com.nai.routinetracker.data.repository.buildSampleTasks
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

@Singleton
class LocalDatabaseSeeder @Inject constructor(
    private val database: RoutineTrackerDatabase,
    private val routineDao: RoutineDao,
    private val taskDao: TaskDao,
    seedStringProvider: FakeSeedStringProvider
) {
    private val seedStrings = seedStringProvider.strings()
    private val mutex = Mutex()

    suspend fun seedIfNeeded() {
        mutex.withLock {
            database.withTransaction {
                val hasExistingData = routineDao.count() > 0 || taskDao.count() > 0
                if (hasExistingData) return@withTransaction

                insertSampleData()
            }
        }
    }

    suspend fun resetToSampleData() {
        mutex.withLock {
            database.withTransaction {
                taskDao.deleteAll()
                routineDao.deleteAll()
                insertSampleData()
            }
        }
    }

    private suspend fun insertSampleData() {
        val routines = buildSampleRoutines(seedStrings)
        val tasks = buildSampleTasks(seedStrings, routines)

        routineDao.insertAll(
            routines.mapIndexed { index, routine ->
                routine.toLocalEntity(sortOrder = index)
            }
        )
        taskDao.insertAll(
            tasks.mapIndexed { index, task ->
                task.toLocalEntity(sortOrder = index)
            }
        )
    }
}
