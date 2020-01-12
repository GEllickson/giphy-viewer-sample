package com.georgeellickson.giphyviewer.di

import android.content.Context
import com.georgeellickson.giphyviewer.network.GiphyApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

@Module
class AppModule(private val context: Context) {

    @Provides
    fun context(): Context = context

    @Provides
    fun giphyApiService(): GiphyApiService {
        val moshi = Moshi.Builder().build()
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl("https://api.giphy.com/v1/")
            .build()
            .create(GiphyApiService::class.java)
    }

}