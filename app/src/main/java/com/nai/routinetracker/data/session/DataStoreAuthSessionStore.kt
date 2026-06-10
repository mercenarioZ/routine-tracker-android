package com.nai.routinetracker.data.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.nai.routinetracker.domain.session.AuthSession
import com.nai.routinetracker.domain.session.AuthSessionStore
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private val Context.authSessionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "auth_session"
)

@Singleton
class DataStoreAuthSessionStore @Inject constructor(
    @ApplicationContext context: Context
) : AuthSessionStore {
    private val dataStore = context.applicationContext.authSessionDataStore

    override fun observeSession(): Flow<AuthSession?> {
        return dataStore.data
            .catch { throwable ->
                if (throwable is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw throwable
                }
            }
            .map { preferences ->
                val accessToken = preferences[ACCESS_TOKEN_KEY]
                val refreshToken = preferences[REFRESH_TOKEN_KEY]
                val tokenType = preferences[TOKEN_TYPE_KEY] ?: DEFAULT_TOKEN_TYPE

                if (accessToken.isNullOrBlank() || refreshToken.isNullOrBlank()) {
                    null
                } else {
                    AuthSession(
                        accessToken = accessToken,
                        refreshToken = refreshToken,
                        tokenType = tokenType
                    )
                }
            }
    }

    override suspend fun saveSession(session: AuthSession) {
        dataStore.edit { preferences ->
            preferences[ACCESS_TOKEN_KEY] = session.accessToken
            preferences[REFRESH_TOKEN_KEY] = session.refreshToken
            preferences[TOKEN_TYPE_KEY] = session.tokenType
        }
    }

    override suspend fun clearSession() {
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    private companion object {
        const val DEFAULT_TOKEN_TYPE = "Bearer"

        val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
        val TOKEN_TYPE_KEY = stringPreferencesKey("token_type")
    }
}
