package com.georgeellickson.giphyviewer.storage

import android.content.Context
import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

private const val PREF_FILE = "giphy_keys"
private const val KEY_API = "giphy_api_key"

@Singleton
class GiphyKeyPref @Inject constructor(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

    fun setApiKey(apiKey: String) = prefs.edit().putString(KEY_API, apiKey).apply()

    fun getApiKey(): String = prefs.getString(KEY_API, "") ?: ""

    fun hasApiKey(): Boolean = getApiKey().isNotEmpty()

    fun clear() = prefs.edit().clear().apply()

}