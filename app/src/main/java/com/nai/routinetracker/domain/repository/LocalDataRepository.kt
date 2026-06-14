package com.nai.routinetracker.domain.repository

interface LocalDataRepository {
    suspend fun resetLocalData()
}
