package com.example.moviedb.data.repository.paging

import android.content.Context
import android.text.format.DateUtils
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import androidx.preference.PreferenceManager
import com.example.moviedb.data.local.dao.MoviesDao
import com.example.moviedb.data.local.dao.TvDao
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.theathre.InTheaterResponse
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.model.tv.OnTvResponse
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.ui.home.paging.InTheatreCallback
import com.example.moviedb.ui.home.paging.OnTvCallback
import com.example.moviedb.until.Listening
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject


private const val TIME_REQUEST_THEATER = "check_time_THEATER"
private const val TIME_REQUEST_TV = "TIME_REQUEST_TV"

class PagingRepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val tvDao: TvDao,
    private val context: Context,
    private val apiServer: TMDBserver,
    private val ioExecutor: Executor
    ): PagingRepository{

    private val boundaryTheatreCallback = InTheatreCallback(
        apiServer,
        ioExecutor,
        this::insertMovieResultIntoDb,
        this::calculateMoviePage,
        context
    )

    private val liveThearePagedList = LivePagedListBuilder<Int, ResultMovie>(moviesDao.getMovies(), Config(20, enablePlaceholders = false))
        .setBoundaryCallback(boundaryTheatreCallback)
        .build()

    override fun getHeadInTheatre(pageSize: Int, language:String): Listening<ResultMovie> {
        return Listening<ResultMovie>(
            pagedList = liveThearePagedList,
            networkState = boundaryTheatreCallback.networkState
        )
    }

    override fun refreshMoviesList() {
        val time = getLastUpdateTime(TIME_REQUEST_THEATER)

        if (time == 0L) {
            clearMovieDB()
            return
        }

        if (isUpdateNeeded(Date(time), 3)){
            clearMovieDB()
        }
    }

    private fun insertMovieResultIntoDb(body: InTheaterResponse?){
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
            saveCurrentTime(TIME_REQUEST_THEATER)
        }
    }

    //------------------------------------------------------------------------------------

    private val boundaryCallback = OnTvCallback(
        apiServer,
        ioExecutor,
        this::insertTvResultIntoDb,
        this::calculateTvPage,
        context
    )

    private val livePagedList = LivePagedListBuilder<Int, ResultTV>(tvDao.getMovies(), 20)
        .setBoundaryCallback(boundaryCallback)
        .build()

    override fun getHeadOnTv(pageSize: Int, language:String): Listening<ResultTV> {
        return Listening(
            livePagedList,
            boundaryCallback.networkState
        )
    }


    override fun refreshTvList() {
        val time = getLastUpdateTime(TIME_REQUEST_TV)

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
            saveCurrentTime(TIME_REQUEST_TV)
        }
    }

    private fun calculateTvPage():Int {
        val size = tvDao.getTableSize()
        return size/20 +1
    }

    private fun insertTvResultIntoDb(body: OnTvResponse?){
        body?.results?.let {
            tvDao.insert(it)
        }
    }

    // ---------------------------------------------

    private fun saveCurrentTime(ker:String){
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = preferences.edit()
        editor.putLong(ker, Date().time)
        editor.apply()
    }

    private fun getLastUpdateTime(key:String):Long{
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)
        return preferences.getLong(key, 0)
    }

    private fun isUpdateNeeded(requestTime:Date, wait:Int):Boolean{
        val after = Date(requestTime.time + wait * DateUtils.HOUR_IN_MILLIS)
        return Date(Date().time).after(after)
    }
}