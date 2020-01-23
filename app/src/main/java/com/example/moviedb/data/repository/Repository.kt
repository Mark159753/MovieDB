package com.example.moviedb.data.repository

import com.example.moviedb.model.ResultMovie
import com.example.moviedb.until.Listening

interface Repository {

    fun getHeadInTheatre(pageSize:Int):Listening<ResultMovie>

    fun refresh()
}