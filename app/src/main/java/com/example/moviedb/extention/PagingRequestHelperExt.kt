package com.example.moviedb.extention

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.moviedb.ui.base.PagingRequestHelper
import com.example.moviedb.until.NetworkState

private fun getErrorMessage(report: PagingRequestHelper.StatusReport): String {
    return PagingRequestHelper.RequestType.values().mapNotNull {
        report.getErrorFor(it)?.message
    }.first()
}

fun PagingRequestHelper.createStatusLiveData():LiveData<NetworkState>{
    val liveData = MutableLiveData<NetworkState>()
    addListener{report ->
        when{
            report.hasRunning() -> liveData.postValue(NetworkState.LOADING)
            report.hasError() -> liveData.postValue(
                NetworkState.ERROR(getErrorMessage(report))
            )
            else -> liveData.postValue(NetworkState.LOADED)
        }
    }
    return liveData
}