package com.example.moviedb.ui.seasons

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.tvDetails.SeasonsDetailsRepository
import javax.inject.Inject

class SeasonViewModel @Inject constructor(
    private val repository: SeasonsDetailsRepository
):ViewModel() {

    fun getEpisodeList(id:Int, seasonNumber:Int, language:String) = repository.getSeasonsDetails(id, seasonNumber, language)
}