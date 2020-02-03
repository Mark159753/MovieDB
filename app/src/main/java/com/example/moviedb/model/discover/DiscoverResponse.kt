package com.example.moviedb.model.discover


import com.google.gson.annotations.SerializedName

data class DiscoverResponse(
    val page: Int,
    val results: List<DiscoverResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)