package com.example.moviedb.data.repository.paging

import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.until.Listening

interface PagingRepository {

    fun getHeadInTheatre(pageSize:Int, language:String): Listening<ResultMovie>

    fun getHeadOnTv(pageSize: Int, language:String):Listening<ResultTV>

    fun refreshTvList()

    fun refreshMoviesList()
}