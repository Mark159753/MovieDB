package com.example.moviedb.data.repository

import android.content.SharedPreferences
import android.text.format.DateUtils
import androidx.paging.LivePagedListBuilder
import com.example.moviedb.data.local.dao.MoviesDao
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.data.paging.InTheatreCallback
import com.example.moviedb.model.InTheaterResponse
import com.example.moviedb.model.ResultMovie
import com.example.moviedb.until.Listening
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

const val TIME_REQUEST = "check_time"

class RepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val apiServer:TMDBserver,
    private val ioExecutor:Executor,
    private val sharedPreferences: SharedPreferences
):Repository {


    private fun insertResultIntoDb(body:InTheaterResponse?){
        body?.results?.let {
            moviesDao.insert(it)
        }
    }

    private fun calculatePage():Int {
        val size = moviesDao.getTableSize()
        return size/20 +1
    }

    override fun refresh() {
        val time = sharedPreferences.getLong(TIME_REQUEST, 0)

        if (time == 0L) {
            clearMovieDB()
            return
        }

        if (isUpdateNeeded(Date(time), 3)){
            clearMovieDB()
        }
    }

    private fun clearMovieDB(){
        ioExecutor.execute {
            moviesDao.cleareMovies()
            saveCurrentTime()
        }
    }

    private fun saveCurrentTime(){
        with(sharedPreferences.edit()) {
            putLong(TIME_REQUEST, Date().time)
            commit()
        }
    }

    override fun getHeadInTheatre(pageSize: Int): Listening<ResultMovie> {
        val boundaryCallback = InTheatreCallback(
            apiServer,
            ioExecutor,
            this::insertResultIntoDb,
            this::calculatePage,
            "uk-uk"
        )

        val livePagedList = LivePagedListBuilder<Int, ResultMovie>(moviesDao.getMovies(), pageSize)
            .setBoundaryCallback(boundaryCallback)
            .build()

        return Listening<ResultMovie>(
            pagedList = livePagedList,
            networkState = boundaryCallback.networkState
        )
    }

    private fun isUpdateNeeded(requestTime:Date, wait:Int):Boolean{
        val after = Date(requestTime.time + wait * DateUtils.HOUR_IN_MILLIS)
        return Date(Date().time).after(after)
    }
}