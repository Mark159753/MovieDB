package com.example.moviedb.model.similarMovies


import com.google.gson.annotations.SerializedName

data class SimilarMoviesResponse(
    val page: Int,
    val results: List<Result>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)