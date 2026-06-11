package com.nai.routinetracker.data.remote.service

import com.nai.routinetracker.data.remote.ApiRoutes
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface RoutineService {
    @GET(ApiRoutes.Routines.LIST)
    suspend fun getRoutines(
        @QueryMap queryParameters: Map<String, String>,
        @Header("Authorization") authorizationHeader: String
    ): Response<ResponseBody>
}
