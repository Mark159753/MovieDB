package com.example.moviedb.ui.discover.paging

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.discover.DiscoverResult
import java.util.*


class DataSourceFactory(
    private val apiServer: TMDBserver,
    private val language:String = "en-US",
    private val sort_by:String = "popularity.desc",
    private val with_genres:List<Int>? = null,
    private val without_genres:List<Int>? = null,
    private val primary_release_date_from: String? = null,
    private val primary_release_date_to: String? = null
)
    : DataSource.Factory<Int, DiscoverResult>(){

    val sourceLiveData = MutableLiveData<PageDiscoverDataSource>()

    override fun create(): DataSource<Int, DiscoverResult> {
        val source = PageDiscoverDataSource(
            apiServer,
            language,
            sort_by,
            with_genres,
            without_genres,
            primary_release_date_from,
            primary_release_date_to
        )
        sourceLiveData.postValue(source)
        return source
    }
}