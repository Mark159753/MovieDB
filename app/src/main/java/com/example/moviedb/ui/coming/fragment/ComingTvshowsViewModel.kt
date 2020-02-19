package com.example.moviedb.ui.coming.fragment

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.Repository
import com.example.moviedb.data.repository.discover.DiscoverRepository
import com.example.moviedb.data.repository.paging.PagingRepository
import com.example.moviedb.until.LocaleHelper
import javax.inject.Inject

class ComingTvshowsViewModel @Inject constructor(
    context: Context,
    private val pagingRepository: PagingRepository,
    private val genreRepo: DiscoverRepository
): ViewModel() {

    private val tvListResult = pagingRepository.getHeadOnTv(20, LocaleHelper.getLanguage(context))

    val resultTv = tvListResult.pagedList
    val networkStateTv = tvListResult.networkState

    fun refreshTv(){
        pagingRepository.refreshTvList()
    }

    fun getGenres() = genreRepo.getGenres()
}
