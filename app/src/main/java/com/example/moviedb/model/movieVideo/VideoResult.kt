package com.example.moviedb.model.movieVideo


import com.google.gson.annotations.SerializedName

data class VideoResult(
    val id: String,
    @SerializedName("iso_3166_1")
    val iso31661: String,
    @SerializedName("iso_639_1")
    val iso6391: String,
    val key: String,
    val name: String,
    val site: String,
    val size: Int,
    val type: String
)