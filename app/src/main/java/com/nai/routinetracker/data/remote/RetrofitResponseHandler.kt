package com.nai.routinetracker.data.remote

import com.nai.routinetracker.data.remote.dto.ApiErrorDto
import com.nai.routinetracker.data.remote.dto.ApiResponseDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.JsonElement
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Response

private val JsonMediaType = "application/json; charset=utf-8".toMediaType()

internal fun String.toJsonRequestBody(): RequestBody {
    return toRequestBody(JsonMediaType)
}

internal suspend fun <T> Response<ResponseBody>.parseApiResponse(
    parseData: (JsonElement?) -> T?
): ApiResponseDto<T> = withContext(Dispatchers.IO) {
    val responseBody = if (isSuccessful) {
        body()?.string().orEmpty()
    } else {
        errorBody()?.string().orEmpty()
    }

    if (isSuccessful) {
        ApiResponseDto.fromJson(
            jsonString = responseBody,
            parseData = parseData
        )
    } else {
        val errorMessage = ApiErrorDto.fromJson(responseBody).displayMessage
            ?: "Request failed with HTTP ${code()}"
        throw ApiException(statusCode = code(), message = errorMessage)
    }
}
