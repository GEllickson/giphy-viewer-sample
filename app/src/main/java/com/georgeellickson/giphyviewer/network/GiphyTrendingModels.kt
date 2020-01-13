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
    @Json(name = "downsized_still") val stillImage: ImageMediumStill,
    @Json(name = "downsized") val gif: ImageMediumGif,
    @Json(name = "preview") val preview: ImagePreview
)

@JsonClass(generateAdapter = true)
data class ImageMediumStill(
    val url: String
)

@JsonClass(generateAdapter = true)
data class ImageMediumGif(
    val url: String
)

@JsonClass(generateAdapter = true)
data class ImagePreview(
    val mp4: String
)