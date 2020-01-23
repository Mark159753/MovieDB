package com.example.moviedb.ui.home

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.Repository
import javax.inject.Inject

class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val repoResult = repository.getHeadInTheatre(20)

    val resultMovies = repoResult.pagedList
    val networkState = repoResult.networkState

    fun refresh(){
        repository.refresh()
    }
}
