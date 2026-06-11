package com.nai.routinetracker.di

import com.nai.routinetracker.data.remote.ApiConfig
import com.nai.routinetracker.data.remote.service.AuthService
import com.nai.routinetracker.data.remote.service.RoutineService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.concurrent.TimeUnit
import javax.inject.Singleton
import okhttp3.OkHttpClient
import retrofit2.Retrofit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(ApiConfig.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .readTimeout(ApiConfig.TIMEOUT_MILLIS, TimeUnit.MILLISECONDS)
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .header("Accept", "*/*")
                    .build()
                chain.proceed(request)
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoutineService(retrofit: Retrofit): RoutineService {
        return retrofit.create(RoutineService::class.java)
    }
}
