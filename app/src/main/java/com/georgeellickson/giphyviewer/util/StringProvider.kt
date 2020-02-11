package com.georgeellickson.giphyviewer.util

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StringProvider @Inject constructor(private val context: Context) {

    fun getString(@StringRes resId: Int): String = context.getString(resId)

    fun getString(@StringRes resId: Int, vararg args: Any?) = context.getString(resId, *args)

}