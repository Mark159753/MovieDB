package com.example.moviedb.ui.discover.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.PageKeyedDataSource
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.discover.DiscoverResult
import com.example.moviedb.until.NetworkState
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.util.*

class PageDiscoverDataSource(
    private val apiServer:TMDBserver,
    private val language:String = "en-US",
    private val sort_by:String = "popularity.desc",
    private val with_genres:List<Int>? = null,
    private val without_genres:List<Int>? = null,
    private val primary_release_date_from: String? = null,
    private val primary_release_date_to:String? = null
    ): PageKeyedDataSource<Int, DiscoverResult>() {

    val networkState = MutableLiveData<NetworkState>()


    override fun loadInitial(
        params: LoadInitialParams<Int>,
        callback: LoadInitialCallback<Int, DiscoverResult>
    ) {
        val request = apiServer.getDiscoverMovie(
            language = this.language,
            sort_by = this.sort_by,
            with_genres = this.with_genres,
            without_genres = this.without_genres,
            primary_release_date_from = this.primary_release_date_from,
            primary_release_date_to = this.primary_release_date_to
        )

        networkState.postValue(NetworkState.LOADING)

        try {
            val response = request.execute()
            val data = response.body()
            val items = data?.results ?: emptyList()
            networkState.postValue(NetworkState.LOADED)
            callback.onResult(items, 1, 2)
        }catch (e:IOException){
            networkState.postValue(NetworkState.ERROR(e.message ?: "unknown error"))
        }
    }

    override fun loadAfter(params: LoadParams<Int>, callback: LoadCallback<Int, DiscoverResult>) {
        networkState.postValue(NetworkState.LOADING)
        val request = apiServer.getDiscoverMovie(
            language = this.language,
            page = params.key,
            sort_by = this.sort_by,
            with_genres = this.with_genres,
            without_genres = this.without_genres,
            primary_release_date_from = this.primary_release_date_from,
            primary_release_date_to = this.primary_release_date_to
        )

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

    override fun loadBefore(params: LoadParams<Int>, callback: LoadCallback<Int, DiscoverResult>) {
        // ignore this
    }
}