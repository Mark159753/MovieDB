package com.example.moviedb.model.genre


import com.google.gson.annotations.SerializedName

data class GenreResponse(
    val genres: List<Genre>
)