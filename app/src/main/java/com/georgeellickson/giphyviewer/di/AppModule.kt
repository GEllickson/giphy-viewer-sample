package com.georgeellickson.giphyviewer.di

import com.georgeellickson.giphyviewer.network.GiphyApiService
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    fun giphyApiService(): GiphyApiService {
        val logger = HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }
        val client = OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
        val moshi = Moshi.Builder().build()
        return Retrofit.Builder()
            .client(client)
            .baseUrl("https://api.giphy.com/v1/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(GiphyApiService::class.java)
    }

}