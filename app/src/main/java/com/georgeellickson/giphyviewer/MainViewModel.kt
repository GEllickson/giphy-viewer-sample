package com.georgeellickson.giphyviewer

import com.georgeellickson.giphyviewer.storage.GiphyKeyPref
import javax.inject.Inject

class MainViewModel @Inject constructor(private val pref: GiphyKeyPref) {

    // Function instead of LiveData as we should populate the first fragment immediately
    fun getStartState(): LaunchStartState =
        if (pref.hasApiKey()) {
            LaunchStartState.HOME
        } else {
            LaunchStartState.SETTINGS
        }

}

enum class LaunchStartState {
    HOME, SETTINGS
}