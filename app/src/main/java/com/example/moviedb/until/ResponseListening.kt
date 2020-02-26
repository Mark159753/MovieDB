package com.example.moviedb.until

import androidx.lifecycle.LiveData

data class ResponseListening<T>(
    val response:LiveData<T>? = null,
    val networkState: LiveData<NetworkState>
)