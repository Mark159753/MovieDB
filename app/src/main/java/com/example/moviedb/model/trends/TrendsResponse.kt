package com.example.moviedb.model.trends


import com.google.gson.annotations.SerializedName

data class TrendsResponse(
    val page: Int,
    val results: List<TrendResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)