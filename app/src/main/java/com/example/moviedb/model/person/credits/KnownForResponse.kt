package com.example.moviedb.model.person.credits


data class KnownForResponse(
    val cast: List<KnownForCast>,
    val id: Int
)