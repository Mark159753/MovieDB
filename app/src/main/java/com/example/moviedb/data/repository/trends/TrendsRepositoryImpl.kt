package com.example.moviedb.data.repository.trends

import android.content.SharedPreferences
import android.text.format.DateUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import com.example.moviedb.data.local.dao.TrendsDao
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.trends.TrendResult
import com.example.moviedb.model.trends.TrendsResponse
import com.example.moviedb.ui.trendsMore.paging.TrendDataSourceFactory
import com.example.moviedb.until.Listening
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import java.util.concurrent.Executor
import javax.inject.Inject


class TrendsRepositoryImpl @Inject constructor(
    private val trendsDao: TrendsDao,
    private val apiServer: TMDBserver,
    private val ioExecutor: Executor,
    private val sharedPreferences: SharedPreferences
): TrendsRepository {

    override fun getTrendsOfQuantity(quantity: Int): LiveData<List<TrendResult>> {
        val time = sharedPreferences.getLong(TrendsRepository.TIME_REQUEST_TRENDS_MOVIE, 0)

        return if (time == 0L || isUpdateNeeded(Date(time), 3)){
            loadTrendsOfType(language = "uk-uk", mediaType = "movie", timeRequest = TrendsRepository.TIME_REQUEST_TRENDS_MOVIE)
            trendsDao.getTrendsOfQuantity(quantity, "movie")
        }else{
            trendsDao.getTrendsOfQuantity(quantity, "movie")
        }
    }

    override fun getTrendsOfMediaType(language: String, mediaType: String, timeWindow:String): Listening<TrendResult> {
        val sourceFactory = TrendDataSourceFactory(apiServer, language, mediaType, timeWindow)

        val livePageList = LivePagedListBuilder<Int, TrendResult>(sourceFactory,
            Config(20)
        ).setFetchExecutor(ioExecutor)
            .build()
        return Listening<TrendResult>(
            pagedList = livePageList,
            networkState = sourceFactory.sourceLiveData.switchMap { it.networkState }
        )
    }



    private fun updateTrendsOfType(list: List<TrendResult>, mediaType: String){
        ioExecutor.execute {
            trendsDao.deleteTrendsOfType(mediaType)
            trendsDao.insert(list)
        }
    }


    private fun loadTrendsOfType(language: String = "uk-uk", mediaType: String, timeRequest:String){
        apiServer.getMovieTrends(language = language, media_type = mediaType)
            .enqueue(object : Callback<TrendsResponse> {
                override fun onFailure(call: Call<TrendsResponse>, t: Throwable) {
                    Log.e("ERROR LOAD TRENDS", t.message)
                }

                override fun onResponse(
                    call: Call<TrendsResponse>,
                    response: Response<TrendsResponse>
                ) {
                    if (response.isSuccessful){
                        updateTrendsOfType(response.body()!!.results, mediaType)
                        saveCurrentTime(timeRequest)
                    }else{
                        Log.e("ERROR LOAD TRENDS", response.code().toString())
                    }

                }
            })
    }



    // ----------------------------------------------------------------------------------------

    private fun saveCurrentTime(key:String){
        with(sharedPreferences.edit()) {
            putLong(key, java.util.Date().time)
            commit()
        }
    }

    private fun isUpdateNeeded(requestTime: Date, wait:Int):Boolean{
        val after = Date(requestTime.time + wait * DateUtils.HOUR_IN_MILLIS)
        return Date(Date().time).after(after)
    }
}