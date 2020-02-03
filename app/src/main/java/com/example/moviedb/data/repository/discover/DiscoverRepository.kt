package com.example.moviedb.data.repository.discover

import androidx.lifecycle.LiveData
import com.example.moviedb.model.discover.DiscoverResult
import com.example.moviedb.model.genre.Genre
import com.example.moviedb.until.Listening
import java.util.*

interface DiscoverRepository {

    fun getDiscoverItems(
        language:String = "en-US",
        sort_by:String = "popularity.desc",
        with_genres:List<Int>? = null,
        without_genres:List<Int>? = null,
        primary_release_date_from: String? = null,
        primary_release_date_to: String? = null
    ):Listening<DiscoverResult>

    fun getGenres():LiveData<List<Genre>>
}