package com.nai.routinetracker.data.repository

import com.nai.routinetracker.data.local.LocalDatabaseSeeder
import com.nai.routinetracker.domain.repository.LocalDataRepository
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomLocalDataRepository @Inject constructor(
    private val seeder: LocalDatabaseSeeder
) : LocalDataRepository {
    override suspend fun resetLocalData() {
        withContext(Dispatchers.IO) {
            seeder.resetToSampleData()
        }
    }
}
