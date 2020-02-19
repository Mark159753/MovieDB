package com.example.moviedb.ui.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.Repository
import com.example.moviedb.data.repository.paging.PagingRepository
import com.example.moviedb.data.repository.trends.TrendsRepository
import com.example.moviedb.model.trends.TrendResult
import com.example.moviedb.until.LocaleHelper
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val context: Context,
    private val repository: Repository,
    private val trendsRepository: TrendsRepository,
    private val pagingRepository: PagingRepository
) : ViewModel() {

    private val moviesListResult = pagingRepository.getHeadInTheatre(20, LocaleHelper.getLanguage(context))

    val resultMovies = moviesListResult.pagedList
    val networkStateMovie = moviesListResult.networkState

    private val tvListResult = pagingRepository.getHeadOnTv(20, LocaleHelper.getLanguage(context))

    val resultTv = tvListResult.pagedList
    val networkStateTv = tvListResult.networkState

    val popularMovies = repository.getPopularMovies(LocaleHelper.getLanguage(context))

    val trendsOfDay = trendsRepository.getTrendsOfQuantity(3, LocaleHelper.getLanguage(context))

    fun refreshTv(){
        pagingRepository.refreshTvList()
    }

    fun updateTrendOfDay():String{
       trendsRepository.loadTrendsOfType(LocaleHelper.getLanguage(context), "media", TrendsRepository.TIME_REQUEST_TRENDS_MOVIE)
        return ""
    }

    fun loadPopularMovies() = repository.loadPopularMovies(LocaleHelper.getLanguage(context))

    fun refreshMovies(){
        pagingRepository.refreshMoviesList()
    }
}
