package com.example.moviedb.model.cast


import com.google.gson.annotations.SerializedName

data class CastResponse(
    val cast: List<Cast>,
    val id: Int
)