package com.georgeellickson.giphyviewer.network

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GiphyTrendingHolder(val data: List<GiphyTrendingItem>)

// TODO add the rest of the fields
@JsonClass(generateAdapter = true)
data class GiphyTrendingItem(
    val id: String,
    val url: String
)