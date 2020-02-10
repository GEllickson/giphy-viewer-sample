package com.georgeellickson.giphyviewer.network

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class GiphyTrendingHolder(val data: List<GiphyTrendingItem>)

// TODO add the rest of the fields
@JsonClass(generateAdapter = true)
data class GiphyTrendingItem(
    val id: String,
    val url: String,
    val images: ItemImages
)

@JsonClass(generateAdapter = true)
data class ItemImages(
    @Json(name = "fixed_height_still") val stillImage: ImageMediumStill,
    @Json(name = "fixed_height") val gif: ImageMediumGif
)

@JsonClass(generateAdapter = true)
data class ImageMediumStill(
    val url: String
)

@JsonClass(generateAdapter = true)
data class ImageMediumGif(
    val url: String
)