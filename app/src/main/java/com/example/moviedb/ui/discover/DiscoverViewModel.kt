package com.example.moviedb.ui.discover

import androidx.lifecycle.ViewModel
import com.example.moviedb.data.repository.discover.DiscoverRepository
import com.example.moviedb.model.discover.DiscoverResult
import com.example.moviedb.until.Listening
import java.util.*
import javax.inject.Inject

class DiscoverViewModel @Inject constructor(
    private val repository: DiscoverRepository
) : ViewModel() {

    fun getDiscoveryList(language:String = "en-US",
                         sort_by:String = "popularity.desc",
                         with_genres:List<Int>? = null,
                         without_genres:List<Int>? = null,
                         primary_release_date_from: String? = null,
                         primary_release_date_to: String? = null):Listening<DiscoverResult>{
        return repository.getDiscoverItems(
            language,
            sort_by,
            with_genres,
            without_genres,
            primary_release_date_from,
            primary_release_date_to
        )
    }

    fun getGenres() = repository.getGenres()

}
