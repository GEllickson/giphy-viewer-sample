package com.georgeellickson.giphyviewer.home

import androidx.lifecycle.*
import com.georgeellickson.giphyviewer.network.ApiResponse
import com.georgeellickson.giphyviewer.network.GiphyTrendingItem
import com.georgeellickson.giphyviewer.storage.GiphyRepository
import com.georgeellickson.giphyviewer.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class HomeViewModel(private val giphyRepo: GiphyRepository) : ViewModel() {

    private val _trendingGifs = MutableLiveData<List<GiphyTrendingItem>>()
    val trendingGifs: LiveData<List<GiphyTrendingItem>>
        get() = _trendingGifs

    private val _navigateToSettings = SingleLiveEvent<Unit>()
    val navigateToSettings: LiveData<Unit>
        get() = _navigateToSettings

    init {
        viewModelScope.launch {
            loadGifs()
        }
    }

    private suspend fun loadGifs() {
        when (val response = giphyRepo.refreshTrendingGifs()) {
            is ApiResponse.Success -> {
                _trendingGifs.postValue(response.data)
            }
            is ApiResponse.Error -> {
                // TODO use observable error
            }
        }
    }

    fun clearApiKey() {
        giphyRepo.clearApiKey()
        _navigateToSettings.callOnChanged()
    }

    class Factory @Inject constructor(private val repo: GiphyRepository) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repo) as T
        }
    }

}