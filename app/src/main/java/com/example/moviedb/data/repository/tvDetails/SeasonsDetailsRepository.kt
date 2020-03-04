package com.example.moviedb.data.repository.tvDetails

import androidx.lifecycle.LiveData
import com.example.moviedb.model.seasons.SeasonsDetailsResponse

interface SeasonsDetailsRepository {

    fun getSeasonsDetails(id:Int, seasonNumber:Int, language:String):LiveData<SeasonsDetailsResponse>
}