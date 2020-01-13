package com.georgeellickson.giphyviewer.storage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.georgeellickson.giphyviewer.network.GiphyApiService
import com.georgeellickson.giphyviewer.network.GiphyTrendingItem
import retrofit2.HttpException
import javax.inject.Inject

class GiphyRepository @Inject constructor(
    private val keyPref: GiphyKeyPref,
    private val apiService: GiphyApiService
) {

    private val _trendingGifs = MutableLiveData<List<GiphyTrendingItem>>()

    val trendingGifs: LiveData<List<GiphyTrendingItem>>
        get() = _trendingGifs

    suspend fun refreshTrendingGifs() {
        try {
            val gifList: List<GiphyTrendingItem> = apiService.getTrending(keyPref.getApiKey()).data
            _trendingGifs.postValue(gifList)
        } catch (e: HttpException) {
            // TODO handle errors
        }
    }

}