package com.georgeellickson.giphyviewer.network

sealed class ApiResponse<T> {
    class Success<T>(val data: T) : ApiResponse<T>()
    class Error<T>(val error: Exception) : ApiResponse<T>()
}

inline fun <T> getApiResponse(response: () -> T): ApiResponse<T> {
    return try {
        ApiResponse.Success(response())
    } catch (e: Exception) {
        ApiResponse.Error(e)
    }
}