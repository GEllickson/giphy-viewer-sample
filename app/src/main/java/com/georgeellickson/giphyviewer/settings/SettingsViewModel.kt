package com.georgeellickson.giphyviewer.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.georgeellickson.giphyviewer.storage.GiphyKeyPref
import com.georgeellickson.giphyviewer.util.SingleLiveEvent
import javax.inject.Inject

class SettingsViewModel(private val pref: GiphyKeyPref) : ViewModel() {

    private val _navigateToHome = SingleLiveEvent<Unit>()
    val navigateToHome: LiveData<Unit>
        get() = _navigateToHome

    fun setKey(key: String) {
        if (key.isNotEmpty()) {
            pref.setApiKey(key)
            _navigateToHome.callOnChanged()
        }
        // TODO check for valid key, give error
    }

}

class SettingsViewModelFactory @Inject constructor(private val pref: GiphyKeyPref) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return SettingsViewModel(pref) as T
    }
}
