package com.example.moviedb.ui.detaile.pagging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.similarMovies.Result
import com.example.moviedb.until.NetworkState
import java.io.IOException

class SimilarMoviesDataSource(
    private val apiServer: TMDBserver,
    private val movieId:Int,
    private val language: String
):PageKeyedDataSource<Int, Result>() {

    val networkState = MutableLiveData<NetworkState>()

    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, Result>
    ) {
        val request = apiServer.getSimilarMovies(
            movie_id = this.movieId,
            language = language,
            page = 1)
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

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
        val request = apiServer.getSimilarMovies(
            movie_id = this.movieId,
            language = language,
            page = params.key)
        networkState.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            val data = response.body()
            val items = data?.results ?: emptyList()
            networkState.postValue(NetworkState.LOADED)
            val pageC = data!!.page + 1
            callback.onResult(items, pageC)
        }catch (e: IOException){
            networkState.postValue(NetworkState.ERROR(e.message ?: "unknown error"))
        }
    }

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, Result>) {
        // Igor this state
    }
}