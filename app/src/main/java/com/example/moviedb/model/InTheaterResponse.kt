package com.example.moviedb.model


import com.google.gson.annotations.SerializedName

data class InTheaterResponse(
    val dates: Dates,
    val page: Int,
    val results: List<ResultMovie>,
    @SerializedName("total_pages")
    val totalPages: Int,
    @SerializedName("total_results")
    val totalResults: Int
)