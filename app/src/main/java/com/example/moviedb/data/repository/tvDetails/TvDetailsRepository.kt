package com.example.moviedb.data.repository.tvDetails

import androidx.lifecycle.LiveData
import com.example.moviedb.model.cast.Cast
import com.example.moviedb.model.movieVideo.VideoResult
import com.example.moviedb.model.tvDetails.TvDetailsResponse

interface TvDetailsRepository {

    fun getTvDetails(id:Int, language:String):LiveData<TvDetailsResponse>

    fun getTvCasts(id:Int, language:String):LiveData<List<Cast>>

    fun getTvVideos(id:Int, language: String):LiveData<List<VideoResult>>
}