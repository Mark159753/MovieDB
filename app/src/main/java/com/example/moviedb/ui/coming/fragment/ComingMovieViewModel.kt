package com.example.moviedb.ui.coming.fragment

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.Repository
import com.example.moviedb.data.repository.discover.DiscoverRepository
import javax.inject.Inject

class ComingMovieViewModel @Inject constructor(
    private val repository:Repository,
    private val genreRepo:DiscoverRepository
): ViewModel() {

    private val moviesListResult = repository.getHeadInTheatre(20)

    val resultMovies = moviesListResult.pagedList
    val networkStateMovie = moviesListResult.networkState

    fun refreshMovies(){
        repository.refreshMoviesList()
    }

    fun getGenres() = genreRepo.getGenres()
}
