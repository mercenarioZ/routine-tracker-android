package com.nai.routinetracker.di

import com.nai.routinetracker.data.repository.AndroidFakeSeedStringProvider
import com.nai.routinetracker.data.repository.FakeSeedStringProvider
import com.nai.routinetracker.data.repository.RemoteAuthRepository
import com.nai.routinetracker.data.repository.RemoteRoutineRepository
import com.nai.routinetracker.data.repository.RemoteTaskRepository
import com.nai.routinetracker.data.settings.DataStoreSettingsRepository
import com.nai.routinetracker.data.session.DataStoreAuthSessionStore
import com.nai.routinetracker.domain.repository.AuthRepository
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.domain.repository.SettingsRepository
import com.nai.routinetracker.domain.repository.TaskRepository
import com.nai.routinetracker.domain.session.AuthSessionStore
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindAuthSessionStore(
        dataStoreAuthSessionStore: DataStoreAuthSessionStore
    ): AuthSessionStore

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        remoteAuthRepository: RemoteAuthRepository
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindRoutineRepository(
        remoteRoutineRepository: RemoteRoutineRepository
    ): RoutineRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        remoteTaskRepository: RemoteTaskRepository
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindSettingsRepository(
        dataStoreSettingsRepository: DataStoreSettingsRepository
    ): SettingsRepository

    @Binds
    @Singleton
    abstract fun bindFakeSeedStringProvider(
        androidFakeSeedStringProvider: AndroidFakeSeedStringProvider
    ): FakeSeedStringProvider
}
