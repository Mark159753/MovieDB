package com.example.moviedb.data.repository

import androidx.lifecycle.LiveData
import com.example.moviedb.model.popular.PopularResult
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.model.trendsOfDay.TrendResult
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.until.Listening

interface Repository {

    fun getHeadInTheatre(pageSize:Int):Listening<ResultMovie>

    fun refreshMoviesList()

    fun getHeadOnTv(pageSize: Int):Listening<ResultTV>

    fun refreshTvList()

    fun getPopularMovies():LiveData<List<PopularResult>>

    fun getTrendsOfQuantity(quantity:Int):LiveData<List<TrendResult>>
}