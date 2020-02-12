package com.example.moviedb.data.repository.trends

import androidx.lifecycle.LiveData
import com.example.moviedb.model.trends.TrendResult
import com.example.moviedb.until.Listening

interface TrendsRepository {
    companion object {
        val TIME_REQUEST_TRENDS_MOVIE = "TIME_REQUEST_TRENDS_PEOPLE"

        const val TREND_MOVIE_TYPE = "movie"
        const val TREND_TV_SHOW_TYPE = "tv"
        const val TREND_PERSON_TYPE = "person"
        const val TREND_ALL_TYPE = "all"

        const val DAY_TIME_WINDOW = "day"
        const val WEEK_TIME_WINDOW = "week"
    }

    fun getTrendsOfQuantity(quantity: Int): LiveData<List<TrendResult>>

    fun getTrendsOfMediaType(language: String, mediaType: String, timeWindow:String): Listening<TrendResult>
}