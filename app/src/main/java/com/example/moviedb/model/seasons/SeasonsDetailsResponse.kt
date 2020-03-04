package com.example.moviedb.model.seasons


import com.google.gson.annotations.SerializedName

data class SeasonsDetailsResponse(
    @SerializedName("air_date")
    val airDate: String,
    val episodes: List<Episode>,
    val id: Int,
    val name: String,
    val overview: String,
    @SerializedName("poster_path")
    val posterPath: String,
    @SerializedName("season_number")
    val seasonNumber: Int
)