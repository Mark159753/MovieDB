package com.example.moviedb.model.tv


import com.google.gson.annotations.SerializedName

data class OnTvResponse(
    val page: Int,
    val results: List<ResultTV>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)