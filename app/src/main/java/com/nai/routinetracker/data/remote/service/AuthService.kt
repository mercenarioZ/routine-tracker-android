package com.nai.routinetracker.data.remote.service

import com.nai.routinetracker.data.remote.ApiRoutes
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST(ApiRoutes.Auth.LOGIN)
    suspend fun login(
        @Body body: RequestBody
    ): Response<ResponseBody>
}
