package com.example.moviedb.model.popular


import com.google.gson.annotations.SerializedName

data class ResponsePopular(
    val page: Int,
    val results: List<PopularResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)