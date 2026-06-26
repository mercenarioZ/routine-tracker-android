package com.nai.routinetracker.di

import com.nai.routinetracker.data.remote.ApiConfig
import com.nai.routinetracker.data.remote.AuthInterceptor
import com.nai.routinetracker.data.remote.TokenRefreshAuthenticator
import com.nai.routinetracker.data.remote.service.AuthService
import com.nai.routinetracker.data.remote.service.RoutineService
import com.nai.routinetracker.data.remote.service.TaskService
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
    @PublicApi
    fun providePublicOkHttpClient(): OkHttpClient {
        return baseOkHttpClientBuilder().build()
    }

    @Provides
    @Singleton
    @AuthenticatedApi
    fun provideAuthenticatedOkHttpClient(
        authInterceptor: AuthInterceptor,
        tokenRefreshAuthenticator: TokenRefreshAuthenticator
    ): OkHttpClient {
        return baseOkHttpClientBuilder()
            .addInterceptor(authInterceptor)
            .authenticator(tokenRefreshAuthenticator)
            .build()
    }

    @Provides
    @Singleton
    @PublicApi
    fun providePublicRetrofit(@PublicApi okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder(okHttpClient)
    }

    @Provides
    @Singleton
    @AuthenticatedApi
    fun provideAuthenticatedRetrofit(@AuthenticatedApi okHttpClient: OkHttpClient): Retrofit {
        return retrofitBuilder(okHttpClient)
    }

    @Provides
    @Singleton
    fun provideAuthService(@PublicApi retrofit: Retrofit): AuthService {
        return retrofit.create(AuthService::class.java)
    }

    @Provides
    @Singleton
    fun provideRoutineService(@AuthenticatedApi retrofit: Retrofit): RoutineService {
        return retrofit.create(RoutineService::class.java)
    }

    @Provides
    @Singleton
    fun provideTaskService(@AuthenticatedApi retrofit: Retrofit): TaskService {
        return retrofit.create(TaskService::class.java)
    }

    private fun baseOkHttpClientBuilder(): OkHttpClient.Builder {
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
    }

    private fun retrofitBuilder(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(ApiConfig.BASE_URL)
            .client(okHttpClient)
            .build()
    }
}
