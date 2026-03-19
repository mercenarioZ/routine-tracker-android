package com.nai.routinetracker.di

import com.nai.routinetracker.data.repository.FakeRoutineRepository
import com.nai.routinetracker.data.repository.RoutineRepository
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
}
