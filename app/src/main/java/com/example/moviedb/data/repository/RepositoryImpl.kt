package com.example.moviedb.data.repository

import android.content.SharedPreferences
import android.text.format.DateUtils
import androidx.paging.LivePagedListBuilder
import com.example.moviedb.data.local.dao.MoviesDao
import com.example.moviedb.data.local.dao.TvDao
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.ui.home.paging.InTheatreCallback
import com.example.moviedb.ui.home.paging.OnTvCallback
import com.example.moviedb.model.InTheaterResponse
import com.example.moviedb.model.OnTvResponse
import com.example.moviedb.model.ResultMovie
import com.example.moviedb.model.ResultTV
import com.example.moviedb.until.Listening
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

const val TIME_REQUEST = "check_time"

class RepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val tvDao:TvDao,
    private val apiServer:TMDBserver,
    private val ioExecutor:Executor,
    private val sharedPreferences: SharedPreferences
):Repository {


    override fun getHeadInTheatre(pageSize: Int): Listening<ResultMovie> {
        val boundaryCallback = InTheatreCallback(
            apiServer,
            ioExecutor,
            this::insertMovieResultIntoDb,
            this::calculateMoviePage,
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

    override fun refreshMoviesList() {
        val time = sharedPreferences.getLong(TIME_REQUEST, 0)

        if (time == 0L) {
            clearMovieDB()
            return
        }

        if (isUpdateNeeded(Date(time), 3)){
            clearMovieDB()
        }
    }

    private fun insertMovieResultIntoDb(body:InTheaterResponse?){
        body?.results?.let {
            moviesDao.insert(it)
        }
    }

    private fun calculateMoviePage():Int {
        val size = moviesDao.getTableSize()
        return size/20 +1
    }

    private fun clearMovieDB(){
        ioExecutor.execute {
            moviesDao.cleareMovies()
            saveCurrentTime()
        }
    }

    // ----------------------------------------------------------------


    override fun getHeadOnTv(pageSize: Int): Listening<ResultTV> {
        val boundaryCallback = OnTvCallback(
            apiServer,
            ioExecutor,
            this::insertTvResultIntoDb,
            this::calculateTvPage,
            "uk-uk"
        )

        val livePagedList = LivePagedListBuilder<Int, ResultTV>(tvDao.getMovies(), pageSize)
            .setBoundaryCallback(boundaryCallback)
            .build()
        return Listening(
            livePagedList,
            boundaryCallback.networkState
        )
    }

    override fun refreshTvList() {
        val time = sharedPreferences.getLong(TIME_REQUEST, 0)

        if (time == 0L) {
            clearTvDB()
            return
        }

        if (isUpdateNeeded(Date(time), 3)){
            clearTvDB()
        }
    }

    private fun clearTvDB(){
        ioExecutor.execute {
            tvDao.clearMovies()
            saveCurrentTime()
        }
    }

    private fun calculateTvPage():Int {
        val size = tvDao.getTableSize()
        return size/20 +1
    }

    private fun insertTvResultIntoDb(body:OnTvResponse?){
        body?.results?.let {
            tvDao.insert(it)
        }
    }

    private fun saveCurrentTime(){
        with(sharedPreferences.edit()) {
            putLong(TIME_REQUEST, Date().time)
            commit()
        }
    }

    private fun isUpdateNeeded(requestTime:Date, wait:Int):Boolean{
        val after = Date(requestTime.time + wait * DateUtils.HOUR_IN_MILLIS)
        return Date(Date().time).after(after)
    }
}