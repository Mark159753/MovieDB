package com.example.moviedb.ui.trendsMore

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.discover.DiscoverRepository
import com.example.moviedb.data.repository.trends.TrendsRepository
import javax.inject.Inject

class TrendsViewModel @Inject constructor(
    private val trendsRepository: TrendsRepository,
    private val genreRepo: DiscoverRepository
): ViewModel() {

    fun getTrendsOfType(mediaType:String, language:String, timeWindow:String) = trendsRepository.getTrendsOfMediaType(language, mediaType, timeWindow)

    fun getGenres() = genreRepo.getGenres()
}