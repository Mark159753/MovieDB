package com.example.moviedb.ui.base

import androidx.paging.PagedList
import com.example.moviedb.extention.createStatusLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

abstract class BasePagingCallback<T, R>(
    private val ioExecutor: Executor,
    private val handlerResponse: (R?) -> Unit
)
    :PagedList.BoundaryCallback<T>(){

    val helper =
        PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    protected fun insertItemIntoDB(response: Response<R>, it: PagingRequestHelper.Request.Callback){
        ioExecutor.execute {
            handlerResponse(response.body())
            it.recordSuccess()
        }
    }

    protected fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
            : Callback<R> {
        return object : Callback<R> {
            override fun onFailure(call: Call<R>, t: Throwable) {
                it.recordFailure(t)
            }

            override fun onResponse(
                call: Call<R>,
                response: Response<R>
            ) {
                insertItemIntoDB(response, it)
            }
        }
    }
}