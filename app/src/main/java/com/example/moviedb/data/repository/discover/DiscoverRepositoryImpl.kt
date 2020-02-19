package com.example.moviedb.data.repository.discover

import android.content.Context
import android.util.Log
import androidx.annotation.MainThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import com.example.moviedb.data.local.dao.GenreDao
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.discover.DiscoverResult
import com.example.moviedb.model.genre.Genre
import com.example.moviedb.model.genre.GenreResponse
import com.example.moviedb.ui.discover.paging.DataSourceFactory
import com.example.moviedb.until.Listening
import com.example.moviedb.until.LocaleHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executor

class DiscoverRepositoryImpl(
    private val context: Context,
    private val apiServer:TMDBserver,
    private val networkExecutor: Executor,
    private val genreDao: GenreDao
):DiscoverRepository{

    @MainThread
    override fun getDiscoverItems(
        language:String,
        sort_by:String,
        with_genres:List<Int>?,
        without_genres:List<Int>?,
        primary_release_date_from: String?,
        primary_release_date_to: String?
    ): Listening<DiscoverResult> {
        val sourceFactory = DataSourceFactory(
            apiServer,
            language,
            sort_by,
            with_genres,
            without_genres,
            primary_release_date_from,
            primary_release_date_to
        )

        val livePagedList = LivePagedListBuilder<Int, DiscoverResult>(sourceFactory,
            Config(20)).setFetchExecutor(networkExecutor)
            .build()

        return Listening(
            pagedList = livePagedList,
            networkState = sourceFactory.sourceLiveData.switchMap { it.networkState }
        )
    }

    override fun getGenres(): LiveData<List<Genre>> {
        networkExecutor.execute {
            val size = genreDao.getTableSize()
            if (size == 0)
                loadGenres()
        }
        return genreDao.getAllGanres()
    }

    private fun loadGenres(){
        apiServer.getGenreList(LocaleHelper.getLanguage(context)).enqueue(object : Callback<GenreResponse>{
            override fun onFailure(call: Call<GenreResponse>, t: Throwable) {
                Log.e("GENRES LOADING ERROR", t.message ?: "unknown error")
            }

            override fun onResponse(call: Call<GenreResponse>, response: Response<GenreResponse>) {
                if (response.isSuccessful){
                    networkExecutor.execute {
                        genreDao.insert(response.body()!!.genres)
                    }
                }else{
                    Log.e("GENRES LOADING ERROR", response.message())
                }
            }
        })
    }
}