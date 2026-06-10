package com.nai.routinetracker.domain.repository

interface AuthRepository {
    suspend fun login(email: String, password: String): Result<Unit>
}
