package com.example.moviedb.ui.tvDetails

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.tvDetails.TvDetailsRepository
import javax.inject.Inject

class TvDetailsViewModel @Inject constructor(
    private val repository: TvDetailsRepository
):ViewModel() {

    fun getTvDetails(id:Int, language:String) = repository.getTvDetails(id, language)

    fun getTvCasts(id:Int, language:String) = repository.getTvCasts(id, language)

    fun getTvVideos(id:Int, language:String) = repository.getTvVideos(id, language)

}