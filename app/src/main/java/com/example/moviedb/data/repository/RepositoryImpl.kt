package com.example.moviedb.data.repository

import android.content.SharedPreferences
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.moviedb.data.local.dao.PopularMoviesDao
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.popular.PopularResult
import com.example.moviedb.model.popular.ResponsePopular
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject

const val TIME_REQUEST_POPULAR = "TIME_REQUEST_POPULAR"

class RepositoryImpl @Inject constructor(
    private val popularMoviesDao: PopularMoviesDao,
    private val apiServer:TMDBserver,
    private val ioExecutor:Executor,
    private val sharedPreferences: SharedPreferences
):Repository {


    override fun getPopularMovies(language:String): LiveData<List<PopularResult>> {
        val time = sharedPreferences.getLong(TIME_REQUEST_POPULAR, 0)

        if (time == 0L || isUpdateNeeded(Date(time), 3)){
            loadPopularMovies(language)
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

    override fun loadPopularMovies(language:String){
        apiServer.getMoviesPopular(language)
            .enqueue(object :Callback<ResponsePopular>{
                override fun onFailure(call: Call<ResponsePopular>, t: Throwable) {
                    Log.e("ERROR LOAD POPULAR", t.message ?: "unknown")
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