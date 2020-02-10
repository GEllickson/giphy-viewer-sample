package com.georgeellickson.giphyviewer.network

import retrofit2.HttpException

sealed class ApiResponse<T> {
    class Success<T>(val data: T) : ApiResponse<T>()
    class Error<T>(error: Throwable) : ApiResponse<T>()
}

inline fun <T> getApiResponse(response: () -> T): ApiResponse<T> {
    return try {
        ApiResponse.Success(response())
    } catch (e: HttpException) {
        ApiResponse.Error(e)
    }
}