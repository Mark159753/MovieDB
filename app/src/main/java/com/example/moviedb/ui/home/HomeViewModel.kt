package com.example.moviedb.ui.home

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.Repository
import com.example.moviedb.data.repository.trends.TrendsRepository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository,
    private val trendsRepository: TrendsRepository
) : ViewModel() {

    private val moviesListResult = repository.getHeadInTheatre(20)

    val resultMovies = moviesListResult.pagedList
    val networkStateMovie = moviesListResult.networkState

    private val tvListResult = repository.getHeadOnTv(20)

    val resultTv = tvListResult.pagedList
    val networkStateTv = tvListResult.networkState

    val popularMovies = repository.getPopularMovies()

    val trendsOfDay = trendsRepository.getTrendsOfQuantity(3)

    fun refreshTv(){
        repository.refreshTvList()
    }

    fun refreshMovies(){
        repository.refreshMoviesList()
    }
}
