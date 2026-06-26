package com.nai.routinetracker.di

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class PublicApi

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class AuthenticatedApi
