package com.example.moviedb.ui.detaile

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.detail.MovieDetailRepository
import com.example.moviedb.model.movieDetail.MovieDetailResponse
import com.example.moviedb.until.ResponseListening
import javax.inject.Inject

class MovieDetailViewModel @Inject constructor(
    private val repository: MovieDetailRepository
): ViewModel() {

    fun getMovieDetails(id:Int, language:String):ResponseListening<MovieDetailResponse>{
        return repository.getMovieDetail(id, language)
    }

    fun getMovieCast(id: Int, language: String) = repository.getMovieCast(id, language)

    fun getSimilarMovies(id:Int, language: String)= repository.getSimilarMovies(id, language)

    fun getMovieVideos(id:Int, language: String) = repository.getMovieVideos(id, language)
}