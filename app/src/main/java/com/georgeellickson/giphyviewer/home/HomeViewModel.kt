package com.georgeellickson.giphyviewer.home

import androidx.lifecycle.*
import com.georgeellickson.giphyviewer.R
import com.georgeellickson.giphyviewer.network.ApiResponse
import com.georgeellickson.giphyviewer.network.GiphyTrendingItem
import com.georgeellickson.giphyviewer.storage.GiphyRepository
import com.georgeellickson.giphyviewer.util.SingleLiveEvent
import com.georgeellickson.giphyviewer.util.StringProvider
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

class HomeViewModel(
    private val giphyRepo: GiphyRepository,
    private val stringProvider: StringProvider
) : ViewModel() {

    private val _loadingSpinnerVisible = MutableLiveData<Boolean>()
    val loadingSpinnerVisible: LiveData<Boolean>
        get() = _loadingSpinnerVisible

    private val _trendingGifs = MutableLiveData<List<GiphyTrendingItem>>()
    val trendingGifs: LiveData<List<GiphyTrendingItem>>
        get() = _trendingGifs

    private val _navigateToSettings = SingleLiveEvent<Unit>()
    val navigateToSettings: LiveData<Unit>
        get() = _navigateToSettings

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    private val isRefreshing = AtomicBoolean(false)

    init {
        giphyRepo.cachedResult?.let {
            _trendingGifs.value = it
        }
        viewModelScope.launch {
            loadGifs()
        }
    }

    private suspend fun loadGifs() {
        _loadingSpinnerVisible.postValue(true)
        val response = giphyRepo.refreshTrendingGifs()
        delay(500)
        when (response) {
            is ApiResponse.Success -> {
                _trendingGifs.postValue(response.data)
            }
            is ApiResponse.Error -> {
                val error = response.error
                if (error is HttpException) {
                    val errorCode = error.code()
                    if (errorCode == 403) {
                        _toastMessage.postValue(stringProvider.getString(R.string.message_invalid_api_token))
                        _navigateToSettings.callOnChanged()
                    } else {
                        _toastMessage.postValue(stringProvider.getString(R.string.message_generic_http_error, errorCode))
                    }
                } else {
                    _toastMessage.postValue(stringProvider.getString(R.string.message_could_not_load_gifs, error.message))
                }
            }
        }
        _loadingSpinnerVisible.postValue(false)
    }

    fun clearApiKey() {
        giphyRepo.clearApiKey()
        _navigateToSettings.callOnChanged()
    }

    fun refreshLatestGifs() {
        if (isRefreshing.compareAndSet(false, true)) {
            _trendingGifs.value = emptyList()
            viewModelScope.launch {
                loadGifs()
                isRefreshing.set(false)
            }
        }
    }

    class Factory @Inject constructor(
        private val repo: GiphyRepository,
        private val stringProvider: StringProvider
    ) : ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repo, stringProvider) as T
        }
    }

}