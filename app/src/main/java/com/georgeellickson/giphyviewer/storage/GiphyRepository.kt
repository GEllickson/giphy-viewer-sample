package com.georgeellickson.giphyviewer.storage

import com.georgeellickson.giphyviewer.network.ApiResponse
import com.georgeellickson.giphyviewer.network.GiphyApiService
import com.georgeellickson.giphyviewer.network.GiphyTrendingItem
import com.georgeellickson.giphyviewer.network.getApiResponse
import javax.inject.Inject

class GiphyRepository @Inject constructor(
    private val keyPref: GiphyKeyPref,
    private val apiService: GiphyApiService
) {

    suspend fun refreshTrendingGifs(): ApiResponse<List<GiphyTrendingItem>> {
        return getApiResponse {
            apiService.getTrending(keyPref.getApiKey()).data
        }
    }

    fun clearApiKey() = keyPref.clear()

}