package com.georgeellickson.giphyviewer.di

import android.content.Context
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val context: Context) {

    @Provides
    fun context(): Context = context

}