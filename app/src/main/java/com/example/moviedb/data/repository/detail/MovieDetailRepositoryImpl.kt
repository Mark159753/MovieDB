package com.example.moviedb.data.repository.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.switchMap
import androidx.paging.Config
import androidx.paging.LivePagedListBuilder
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.cast.CastResponse
import com.example.moviedb.model.movieDetail.MovieDetailResponse
import com.example.moviedb.model.movieVideo.VideoResponse
import com.example.moviedb.model.movieVideo.VideoResult
import com.example.moviedb.model.similarMovies.Result
import com.example.moviedb.ui.detaile.pagging.DetailSimilarMoviesDataSourceFactory
import com.example.moviedb.until.Listening
import com.example.moviedb.until.NetworkState
import com.example.moviedb.until.ResponseListening
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executors
import javax.inject.Inject

class MovieDetailRepositoryImpl @Inject constructor(
    private val server:TMDBserver
):MovieDetailRepository{

    override fun getMovieDetail(id: Int, language: String): ResponseListening<MovieDetailResponse> {
        val networkState = MutableLiveData<NetworkState>().apply { value = NetworkState.LOADING }
        val data = MutableLiveData<MovieDetailResponse>()
        server.getMovieDetails(id, language)
            .enqueue(object :Callback<MovieDetailResponse>{
                override fun onFailure(call: Call<MovieDetailResponse>, t: Throwable) {
                    networkState.postValue(NetworkState.ERROR(t.message ?: "unknown error"))
                }

                override fun onResponse(
                    call: Call<MovieDetailResponse>,
                    response: Response<MovieDetailResponse>
                ) {
                    networkState.postValue(NetworkState.LOADED)
                    data.postValue(response.body())
                }
            })
        return ResponseListening(
            response = data,
            networkState = networkState
        )
    }

    override fun getMovieCast(id: Int, language: String): ResponseListening<CastResponse> {
        val networkState = MutableLiveData<NetworkState>().apply { value = NetworkState.LOADING }
        val result = MutableLiveData<CastResponse>()

        server.getMovieCast(id, language)
            .enqueue(object :Callback<CastResponse>{
                override fun onFailure(call: Call<CastResponse>, t: Throwable) {
                    networkState.postValue(NetworkState.ERROR(t.message ?: "unknown error"))
                }

                override fun onResponse(
                    call: Call<CastResponse>,
                    response: Response<CastResponse>
                ) {
                    networkState.postValue(NetworkState.LOADED)
                    result.postValue(response.body())
                }
            })

        return ResponseListening(
            response = result,
            networkState = networkState
        )
    }

    override fun getSimilarMovies(movieID: Int, language: String): Listening<Result> {
        val sourceFactory = DetailSimilarMoviesDataSourceFactory(server, movieID, language)

        val livePageList = LivePagedListBuilder<Int, Result>(sourceFactory, Config(20))
            .setFetchExecutor(Executors.newSingleThreadExecutor())
            .build()
        return Listening<Result>(
            pagedList = livePageList,
            networkState = sourceFactory.sourceLiveData.switchMap { it.networkState }
        )
    }

    override fun getMovieVideos(movieID: Int, language: String): LiveData<List<VideoResult>> {
        val result = MutableLiveData<List<VideoResult>>()

        server.getMovieVideos(movieID, language)
            .enqueue(object :Callback<VideoResponse>{
                override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                    Log.e("LOAD ERROR", t.message ?: "unknown error")
                }

                override fun onResponse(
                    call: Call<VideoResponse>,
                    response: Response<VideoResponse>
                ) {
                    if (response.isSuccessful){
                        result.postValue(response.body()!!.results)
                    }
                }
            })

        return result
    }
}