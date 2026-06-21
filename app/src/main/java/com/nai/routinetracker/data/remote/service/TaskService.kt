package com.nai.routinetracker.data.remote.service

import com.nai.routinetracker.data.remote.ApiRoutes
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskService {
    @GET(ApiRoutes.Tasks.LIST)
    suspend fun getTasks(
        @Header("Authorization") authorizationHeader: String
    ): Response<ResponseBody>

    @POST(ApiRoutes.Tasks.LIST)
    suspend fun createTask(
        @Body body: RequestBody,
        @Header("Authorization") authorizationHeader: String
    ): Response<ResponseBody>

    @PATCH(ApiRoutes.Tasks.COMPLETION)
    suspend fun updateTaskCompletion(
        @Path("taskId") taskId: String,
        @Body body: RequestBody,
        @Header("Authorization") authorizationHeader: String
    ): Response<ResponseBody>
}
