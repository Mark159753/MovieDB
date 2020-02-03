package com.example.moviedb.ui.home.paging

import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.theathre.InTheaterResponse
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.ui.base.BasePagingCallback
import com.example.moviedb.ui.base.PagingRequestHelper
import java.util.concurrent.Executor

class InTheatreCallback(
    private val apiServer:TMDBserver,
    private val ioExecutor: Executor,
    private val handlerResponse: (InTheaterResponse?) -> Unit,
    private val hadleNextPage: () -> Int,
    private val language:String
): BasePagingCallback<ResultMovie, InTheaterResponse>(ioExecutor, handlerResponse) {

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


}