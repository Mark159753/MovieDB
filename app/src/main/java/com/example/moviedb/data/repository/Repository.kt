package com.example.moviedb.data.repository

import com.example.moviedb.model.ResultMovie
import com.example.moviedb.model.ResultTV
import com.example.moviedb.until.Listening

interface Repository {

    fun getHeadInTheatre(pageSize:Int):Listening<ResultMovie>

    fun refreshMoviesList()

    fun getHeadOnTv(pageSize: Int):Listening<ResultTV>

    fun refreshTvList()
}