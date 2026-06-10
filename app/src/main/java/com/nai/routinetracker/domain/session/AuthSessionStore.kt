package com.nai.routinetracker.domain.session

import kotlinx.coroutines.flow.Flow

interface AuthSessionStore {
    fun observeSession(): Flow<AuthSession?>

    suspend fun saveSession(session: AuthSession)

    suspend fun clearSession()
}
