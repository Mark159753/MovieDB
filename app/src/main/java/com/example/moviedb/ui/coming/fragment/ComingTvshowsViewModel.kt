package com.example.moviedb.ui.coming.fragment

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.Repository
import com.example.moviedb.data.repository.discover.DiscoverRepository
import javax.inject.Inject

class ComingTvshowsViewModel @Inject constructor(
    private val repository: Repository,
    private val genreRepo: DiscoverRepository
): ViewModel() {

    private val tvListResult = repository.getHeadOnTv(20)

    val resultTv = tvListResult.pagedList
    val networkStateTv = tvListResult.networkState

    fun refreshTv(){
        repository.refreshTvList()
    }

    fun getGenres() = genreRepo.getGenres()
}
