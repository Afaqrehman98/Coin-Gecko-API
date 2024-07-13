package com.example.coingeckotask.data.network

import com.example.coingeckotask.data.models.ApiError
import com.example.coingeckotask.utils.ApiException
import com.example.coingeckotask.utils.fromPrettyJson
import retrofit2.Response

abstract class SafeApiRequest {

    suspend fun <T : Any> apiRequest(call: suspend () -> Response<T>): T {
        val response = call.invoke()
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            val errorMessage = "Something went wrong"
            if (response.code() == 429) {
                val errorResponse = response.errorBody()?.string()?.fromPrettyJson<ApiError>()
                throw ApiException(errorResponse?.error?.message ?: errorMessage)
            }
            throw ApiException(errorMessage)
        }
    }
}
