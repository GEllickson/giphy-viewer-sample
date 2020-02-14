package com.georgeellickson.giphyviewer.storage

import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.network.ApiResponse
import com.georgeellickson.giphyviewer.network.GiphyApiService
import com.georgeellickson.giphyviewer.network.GiphyTrendingItem
import com.georgeellickson.giphyviewer.network.getApiResponse
import com.georgeellickson.giphyviewer.util.StringProvider
import retrofit2.HttpException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GiphyRepository @Inject constructor(
    private val keyPref: GiphyKeyPref,
    private val apiService: GiphyApiService,
    private val stringProvider: StringProvider
) {

    var cachedResult: List<GiphyTrendingItem>? = null
        private set

    suspend fun refreshTrendingGifs(): ApiResponse<List<GiphyTrendingItem>> {
        val response = getApiResponse { apiService.getTrending(keyPref.getApiKey()).data }
        if (response is ApiResponse.Success) {
            cachedResult = response.data
        }
        return response
    }

    suspend fun tryApiKey(key: String): ApiKeyResponse {
        keyPref.setApiKey(key)
        val response = refreshTrendingGifs()
        return if (response is ApiResponse.Success) {
            ApiKeyResponse.Success
        } else {
            clearApiKey()
            val exception = (response as ApiResponse.Error).error
            val errorMessage = if (exception is HttpException && exception.code() == 403) {
                stringProvider.getString(R.string.message_invalid_api_token)
            } else {
                exception.message ?: "Error"
            }
            ApiKeyResponse.Failure(errorMessage)
        }
    }

    fun clearApiKey() = keyPref.clear()

    sealed class ApiKeyResponse {
        object Success : ApiKeyResponse()
        class Failure(val error: String) : ApiKeyResponse()
    }

}