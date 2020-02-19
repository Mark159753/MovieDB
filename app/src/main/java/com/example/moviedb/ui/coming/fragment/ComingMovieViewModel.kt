package com.example.moviedb.ui.coming.fragment

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.discover.DiscoverRepository
import com.example.moviedb.data.repository.paging.PagingRepository
import com.example.moviedb.until.LocaleHelper
import javax.inject.Inject

class ComingMovieViewModel @Inject constructor(
    private val context: Context,
    private val genreRepo:DiscoverRepository,
    private val pagingRepo:PagingRepository
): ViewModel() {

    private val moviesListResult = pagingRepo.getHeadInTheatre(20, LocaleHelper.getLanguage(context))

    val resultMovies = moviesListResult.pagedList
    val networkStateMovie = moviesListResult.networkState

    fun refreshMovies(){
        pagingRepo.refreshMoviesList()
    }

    fun getGenres() = genreRepo.getGenres()
}
