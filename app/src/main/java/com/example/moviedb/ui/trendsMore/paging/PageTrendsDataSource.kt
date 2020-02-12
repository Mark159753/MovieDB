package com.example.moviedb.ui.trendsMore.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.trends.TrendResult
import com.example.moviedb.until.NetworkState
import java.io.IOException

class PageTrendsDataSource(
    private val apiServer: TMDBserver,
    private val language:String,
    private val mediaType:String,
    private val timeWindow:String
)
    :PageKeyedDataSource<Int, TrendResult>(){

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, TrendResult>
    ) {
        val request = apiServer.getMovieTrends(
            media_type = mediaType,
            time_window = timeWindow,
            language = language)
        networkState.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            val data = response.body()
            val items = data?.results ?: emptyList()
            networkState.postValue(NetworkState.LOADED)
            callback.onResult(items, 1, 2)
        }catch (e: IOException){
            networkState.postValue(NetworkState.ERROR(e.message ?: "unknown error"))
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, TrendResult>) {
        val request = apiServer.getMovieTrends(
            media_type = mediaType,
            time_window = timeWindow,
            page = params.key,
            language = language)
        networkState.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            val data = response.body()
            val items = data?.results ?: emptyList()
            networkState.postValue(NetworkState.LOADED)
            val pageC = data!!.page + 1
            callback.onResult(items, pageC)
        }catch (e:IOException){
            networkState.postValue(NetworkState.ERROR(e.message ?: "unknown error"))
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, TrendResult>) {
        // ignore it
    }
}