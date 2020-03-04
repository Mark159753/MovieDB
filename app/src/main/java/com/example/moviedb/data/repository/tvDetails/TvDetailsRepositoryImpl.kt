package com.example.moviedb.data.repository.tvDetails

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.cast.Cast
import com.example.moviedb.model.cast.CastResponse
import com.example.moviedb.model.movieVideo.VideoResponse
import com.example.moviedb.model.movieVideo.VideoResult
import com.example.moviedb.model.seasons.SeasonsDetailsResponse
import com.example.moviedb.model.tvDetails.TvDetailsResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class TvDetailsRepositoryImpl @Inject constructor(
    private val server:TMDBserver
): TvDetailsRepository, SeasonsDetailsRepository {

    override fun getTvDetails(id: Int, language: String): LiveData<TvDetailsResponse> {
        val result = MutableLiveData<TvDetailsResponse>()

        server.getTvDetails(id, language)
            .enqueue(object : Callback<TvDetailsResponse>{
                override fun onFailure(call: Call<TvDetailsResponse>, t: Throwable) {
                    Log.e("LOAD_ERROR", t.message ?: "Unknown error")
                }

                override fun onResponse(
                    call: Call<TvDetailsResponse>,
                    response: Response<TvDetailsResponse>
                ) {
                    if (response.isSuccessful){
                        result.postValue(response.body())
                    }
                }
            })
        return result
    }

    override fun getTvCasts(id: Int, language: String): LiveData<List<Cast>> {
        val result = MutableLiveData<List<Cast>>()

        server.getTvCast(id, language).enqueue(object :Callback<CastResponse>{
            override fun onFailure(call: Call<CastResponse>, t: Throwable) {
                Log.e("LOAD_ERROR", t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<CastResponse>, response: Response<CastResponse>) {
                if (response.isSuccessful){
                    result.postValue(response.body()!!.cast)
                }
            }
        })
        return result
    }

    override fun getTvVideos(id: Int, language: String): LiveData<List<VideoResult>> {
        val result = MutableLiveData<List<VideoResult>>()

        server.getTvVideos(id, language).enqueue(object :Callback<VideoResponse>{
            override fun onFailure(call: Call<VideoResponse>, t: Throwable) {
                Log.e("LOAD_ERROR", t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<VideoResponse>, response: Response<VideoResponse>) {
                if (response.isSuccessful){
                    result.postValue(response.body()!!.results)
                }
            }
        })
        return result
    }

    override fun getSeasonsDetails(
        id: Int,
        seasonNumber: Int,
        language: String
    ): LiveData<SeasonsDetailsResponse> {
        val result = MutableLiveData<SeasonsDetailsResponse>()

        server.getTvSeasonDetails(id, seasonNumber, language).enqueue(object :Callback<SeasonsDetailsResponse>{
            override fun onFailure(call: Call<SeasonsDetailsResponse>, t: Throwable) {
                Log.e("LOAD_ERROR", t.message ?: "Unknown error")
            }

            override fun onResponse(call: Call<SeasonsDetailsResponse>, response: Response<SeasonsDetailsResponse>) {
                if (response.isSuccessful){
                    result.postValue(response.body())
                }
            }
        })
        return result
    }
}