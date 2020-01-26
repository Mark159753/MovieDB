package com.example.moviedb.data.repository

import android.content.SharedPreferences
import android.text.format.DateUtils
import android.util.Log
import android.view.textclassifier.TextLanguage
import androidx.lifecycle.LiveData
import androidx.paging.LivePagedListBuilder
import com.example.moviedb.data.local.dao.MoviesDao
import com.example.moviedb.data.local.dao.PopularMoviesDao
import com.example.moviedb.data.local.dao.TrendsDao
import com.example.moviedb.data.local.dao.TvDao
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.popular.PopularResult
import com.example.moviedb.model.popular.ResponsePopular
import com.example.moviedb.ui.home.paging.InTheatreCallback
import com.example.moviedb.ui.home.paging.OnTvCallback
import com.example.moviedb.model.theathre.InTheaterResponse
import com.example.moviedb.model.tv.OnTvResponse
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.model.trendsOfDay.TrendResult
import com.example.moviedb.model.trendsOfDay.TrendsResponse
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.until.Listening
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

const val TIME_REQUEST_THEATER = "check_time_THEATER"
const val TIME_REQUEST_TV = "TIME_REQUEST_TV"
const val TIME_REQUEST_POPULAR = "TIME_REQUEST_POPULAR"
const val TIME_REQUEST_TRENDS = "TIME_REQUEST_TRENDS"

class RepositoryImpl @Inject constructor(
    private val moviesDao: MoviesDao,
    private val tvDao:TvDao,
    private val popularMoviesDao: PopularMoviesDao,
    private val trendsDao: TrendsDao,
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
        val time = sharedPreferences.getLong(TIME_REQUEST_THEATER, 0)

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
        val time = sharedPreferences.getLong(TIME_REQUEST_TV, 0)

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

    //---------------------------------------------


    override fun getPopularMovies(): LiveData<List<PopularResult>> {
        val time = sharedPreferences.getLong(TIME_REQUEST_POPULAR, 0)

        if (time == 0L || isUpdateNeeded(Date(time), 3)){
            loadPopularMovies()
            return popularMoviesDao.getAllPopularMovies()
        }else{
            return popularMoviesDao.getAllPopularMovies()
        }

    }

    private fun updatePopularMoviews(list: List<PopularResult>){
        ioExecutor.execute {
            popularMoviesDao.clearAll()
            popularMoviesDao.insert(list)
        }
    }

    private fun loadPopularMovies(){
        apiServer.getMoviesPopular("uk-uk")
            .enqueue(object :Callback<ResponsePopular>{
                override fun onFailure(call: Call<ResponsePopular>, t: Throwable) {
                    Log.e("ERROR LOAD POPULAR", t.message)
                }

                override fun onResponse(
                    call: Call<ResponsePopular>,
                    response: Response<ResponsePopular>
                ) {
                    if (response.isSuccessful){
                        updatePopularMoviews(response.body()!!.results)
                        saveCurrentTime(TIME_REQUEST_POPULAR)
                    }else{
                        Log.e("ERROR LOAD POPULAR", response.code().toString())
                    }
                }
            })
    }

    //-----------------------------------------------------------------

    override fun getTrendsOfQuantity(quantity: Int): LiveData<List<TrendResult>> {
        val time = sharedPreferences.getLong(TIME_REQUEST_TRENDS, 0)

        return if (time == 0L || isUpdateNeeded(Date(time), 3)){
            loadTrendsOfDay()
            trendsDao.getTrendsOfQuantity(quantity)
        }else{
            trendsDao.getTrendsOfQuantity(quantity)
        }
    }

    private fun updateTrends(list: List<TrendResult>){
        ioExecutor.execute {
            trendsDao.clearTrends()
            trendsDao.insert(list)
        }
    }

    private fun loadTrendsOfDay(language: String = "uk-uk"){
        apiServer.getMovieTrendsOfDay(language)
            .enqueue(object :Callback<TrendsResponse>{
                override fun onFailure(call: Call<TrendsResponse>, t: Throwable) {
                    Log.e("ERROR LOAD TRENDS", t.message)
                }

                override fun onResponse(
                    call: Call<TrendsResponse>,
                    response: Response<TrendsResponse>
                ) {
                    if (response.isSuccessful){
                        updateTrends(response.body()!!.results)
                        saveCurrentTime(TIME_REQUEST_TRENDS)
                    }else{
                        Log.e("ERROR LOAD TRENDS", response.code().toString())
                    }

                }
            })
    }



    private fun saveCurrentTime(key:String){
        with(sharedPreferences.edit()) {
            putLong(key, Date().time)
            commit()
        }
    }

    private fun isUpdateNeeded(requestTime:Date, wait:Int):Boolean{
        val after = Date(requestTime.time + wait * DateUtils.HOUR_IN_MILLIS)
        return Date(Date().time).after(after)
    }
}