package com.example.moviedb.data.repository

import androidx.lifecycle.LiveData
import com.example.moviedb.model.popular.PopularResult
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.model.trends.TrendResult
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.until.Listening

interface Repository {

    fun loadPopularMovies(language:String)

    fun getPopularMovies(language:String):LiveData<List<PopularResult>>

}