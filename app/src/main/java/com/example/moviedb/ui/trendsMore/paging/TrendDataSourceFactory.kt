package com.example.moviedb.ui.trendsMore.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.trends.TrendResult

class TrendDataSourceFactory(
    private val apiServer:TMDBserver,
    private val language:String,
    private val mediaType:String,
    private val timeWindow:String
)
    :DataSource.Factory<Int, TrendResult>(){

    val sourceLiveData = MutableLiveData<PageTrendsDataSource>()

    override fun create(): DataSource<Int, TrendResult> {
        val source = PageTrendsDataSource(apiServer, language, mediaType, timeWindow)
        sourceLiveData.postValue(source)
        return source
    }
}