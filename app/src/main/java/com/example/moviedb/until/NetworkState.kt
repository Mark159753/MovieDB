package com.example.moviedb.until

sealed class NetworkState {
    object LOADING : NetworkState()
    object LOADED : NetworkState()
    data class ERROR(val msg:String): NetworkState()
}