package com.georgeellickson.giphyviewer.settings

import androidx.lifecycle.*
import com.georgeellickson.giphyviewer.storage.GiphyRepository
import com.georgeellickson.giphyviewer.util.SingleLiveEvent
import kotlinx.coroutines.launch
import javax.inject.Inject

class SettingsViewModel(private val repo: GiphyRepository) : ViewModel() {

    private val _loadingState = MutableLiveData<Boolean>()
    val loadingState: LiveData<Boolean>
        get() = _loadingState

    private val _navigateToHome = SingleLiveEvent<Unit>()
    val navigateToHome: LiveData<Unit>
        get() = _navigateToHome

    private val _toastMessage = SingleLiveEvent<String>()
    val toastMessage: LiveData<String>
        get() = _toastMessage

    fun tryApiKey(key: String) {
        if (key.isNotEmpty()) {
            _loadingState.value = true
            viewModelScope.launch {
                when (val result = repo.tryApiKey(key)) {
                    GiphyRepository.ApiKeyResponse.Success -> {
                        _navigateToHome.callOnChanged()
                    }
                    is GiphyRepository.ApiKeyResponse.Failure -> {
                        _toastMessage.value = result.error
                    }
                }
                _loadingState.value = false
            }
        }
    }

    class Factory @Inject constructor(private val repo: GiphyRepository) :
        ViewModelProvider.Factory {

        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(repo) as T
        }
    }

}
