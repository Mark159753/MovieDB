package com.example.moviedb.ui.detaile.pagging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.similarMovies.Result

class DetailSimilarMoviesDataSourceFactory(
    private val apiServer: TMDBserver,
    private val movieId:Int,
    private val language: String
):DataSource.Factory<Int, Result>() {

    val sourceLiveData = MutableLiveData<SimilarMoviesDataSource>()

    override fun create(): DataSource<Int, Result> {
        val source = SimilarMoviesDataSource(apiServer, movieId, language)
        sourceLiveData.postValue(source)
        return source
    }
}