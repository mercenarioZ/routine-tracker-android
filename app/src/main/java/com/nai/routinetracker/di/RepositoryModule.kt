package com.nai.routinetracker.di

import com.nai.routinetracker.data.repository.FakeRoutineRepository
import com.nai.routinetracker.data.repository.FakeTaskRepository
import com.nai.routinetracker.domain.repository.RoutineRepository
import com.nai.routinetracker.domain.repository.TaskRepository
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
    abstract fun bindRoutineRepository(
        fakeRoutineRepository: FakeRoutineRepository
    ): RoutineRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        fakeTaskRepository: FakeTaskRepository
    ): TaskRepository
}
