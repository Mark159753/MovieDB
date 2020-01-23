package com.example.moviedb.until

import androidx.lifecycle.LiveData
import androidx.paging.PagedList

data class Listening<T>(
    val pagedList: LiveData<PagedList<T>>,
    val networkState: LiveData<NetworkState>
)