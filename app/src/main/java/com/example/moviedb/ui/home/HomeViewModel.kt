package com.example.moviedb.ui.home

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.Repository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val moviesListResult = repository.getHeadInTheatre(20)

    val resultMovies = moviesListResult.pagedList
    val networkStateMovie = moviesListResult.networkState

    private val tvListResult = repository.getHeadOnTv(20)

    val resultTv = tvListResult.pagedList
    val networkStateTv = tvListResult.networkState

    fun refreshTv(){
        repository.refreshTvList()
    }

    fun refreshMovies(){
        repository.refreshMoviesList()
    }
}
