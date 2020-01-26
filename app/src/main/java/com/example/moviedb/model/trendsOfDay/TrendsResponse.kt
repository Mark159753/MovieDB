package com.example.moviedb.model.trendsOfDay


import com.google.gson.annotations.SerializedName

data class TrendsResponse(
    val page: Int,
    val results: List<TrendResult>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)