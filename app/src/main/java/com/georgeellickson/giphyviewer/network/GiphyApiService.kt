package com.georgeellickson.giphyviewer.network

import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApiService {

    @GET("gifs/trending")
    suspend fun getTrending(
        @Query("api_key") api_key: String,
        @Query("limit") limit: Int = 40
    ): GiphyTrendingHolder

}