package com.example.moviedb.data.repository.detail

import androidx.lifecycle.LiveData
import com.example.moviedb.model.cast.CastResponse
import com.example.moviedb.model.movieDetail.MovieDetailResponse
import com.example.moviedb.model.movieVideo.VideoResult
import com.example.moviedb.model.similarMovies.Result
import com.example.moviedb.until.Listening
import com.example.moviedb.until.ResponseListening

interface MovieDetailRepository {

    fun getMovieDetail(id:Int, language:String):ResponseListening<MovieDetailResponse>

    fun getMovieCast(id:Int, language:String):ResponseListening<CastResponse>

    fun getSimilarMovies(movieID:Int, language:String):Listening<Result>

    fun getMovieVideos(movieID:Int, language:String):LiveData<List<VideoResult>>
}