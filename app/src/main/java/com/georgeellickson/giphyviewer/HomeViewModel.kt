package com.georgeellickson.giphyviewer

import androidx.lifecycle.*
import com.georgeellickson.giphyviewer.network.GiphyApiService
import com.georgeellickson.giphyviewer.network.GiphyTrendingItem
import com.georgeellickson.giphyviewer.storage.GiphyKeyPref
import kotlinx.coroutines.launch
import retrofit2.HttpException
import timber.log.Timber
import javax.inject.Inject

// TODO inject Repo instead when database is created
class HomeViewModel(
    private val pref: GiphyKeyPref,
    private val apiService: GiphyApiService
) : ViewModel() {

    private val _trendingGifs = MutableLiveData<List<GiphyTrendingItem>>()
    val trendingGifs: LiveData<List<GiphyTrendingItem>>
        get() = _trendingGifs

    init {
        viewModelScope.launch {
            try {
                val result = apiService.getTrending(pref.getApiKey())
                _trendingGifs.value = result.data
            } catch (e: HttpException) {
                // TODO show error message to user
                Timber.d(e)
            }
        }
    }

}

class HomeViewModelFactory @Inject constructor(
    private val pref: GiphyKeyPref,
    private val apiService: GiphyApiService
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return HomeViewModel(pref, apiService) as T
    }
}