package com.nai.routinetracker.data.remote.service

import com.nai.routinetracker.data.remote.ApiRoutes
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.QueryMap

interface RoutineService {
    @GET(ApiRoutes.Routines.LIST)
    suspend fun getRoutines(
        @QueryMap queryParameters: Map<String, String>,
    ): Response<ResponseBody>

    @POST(ApiRoutes.Routines.LIST)
    suspend fun createRoutine(
        @Body body: RequestBody,
    ): Response<ResponseBody>
}
