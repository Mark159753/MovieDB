package com.example.moviedb.data.paging

import androidx.paging.PagedList
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.extention.createStatusLiveData
import com.example.moviedb.model.InTheaterResponse
import com.example.moviedb.model.ResultMovie
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.concurrent.Executor

class InTheatreCallback(
    private val apiServer:TMDBserver,
    private val ioExecutor: Executor,
    private val handlerResponse: (InTheaterResponse?) -> Unit,
    private val hadleNextPage: () -> Int,
    private val language:String
): PagedList.BoundaryCallback<ResultMovie>() {

    val helper = PagingRequestHelper(ioExecutor)
    val networkState = helper.createStatusLiveData()

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
            apiServer.getInTheatre(language)
                .enqueue(createWebserviceCallback(it))
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: ResultMovie) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            ioExecutor.execute {
                val page = hadleNextPage.invoke()
                apiServer.getInTheatre(language, page)
                    .enqueue(createWebserviceCallback(it))
            }
        }
    }

    private fun insertItemIntoDB(response:Response<InTheaterResponse>, it:PagingRequestHelper.Request.Callback){
        ioExecutor.execute {
            handlerResponse(response.body())
            it.recordSuccess()
        }
    }

    private fun createWebserviceCallback(it: PagingRequestHelper.Request.Callback)
        : Callback<InTheaterResponse>{
        return object :Callback<InTheaterResponse>{
            override fun onFailure(call: Call<InTheaterResponse>, t: Throwable) {
                it.recordFailure(t)
            }

            override fun onResponse(
                call: Call<InTheaterResponse>,
                response: Response<InTheaterResponse>
            ) {
                insertItemIntoDB(response, it)
            }
        }
    }
}