package com.georgeellickson.giphyviewer

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.georgeellickson.giphyviewer.storage.GiphyKeyPref
import javax.inject.Inject

class MainViewModel(private val pref: GiphyKeyPref) : ViewModel() {

    fun hasApiKey() = pref.hasApiKey()

}

class MainViewModelFactory @Inject constructor(private val pref: GiphyKeyPref) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainViewModel(pref) as T
    }
}