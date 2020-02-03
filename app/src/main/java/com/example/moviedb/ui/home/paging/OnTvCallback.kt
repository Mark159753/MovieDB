package com.example.moviedb.ui.home.paging

import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.tv.OnTvResponse
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.ui.base.BasePagingCallback
import com.example.moviedb.ui.base.PagingRequestHelper
import java.util.concurrent.Executor

class OnTvCallback(
    private val apiServer: TMDBserver,
    private val ioExecutor: Executor,
    private val handlerResponse: (OnTvResponse?) -> Unit,
    private val hadleNextPage: () -> Int,
    private val language:String
): BasePagingCallback<ResultTV, OnTvResponse>(ioExecutor, handlerResponse) {

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
            apiServer.getOnTv(language)
                .enqueue(createWebserviceCallback(it))
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: ResultTV) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            ioExecutor.execute {
                val page = hadleNextPage.invoke()
                apiServer.getOnTv(language, page)
                    .enqueue(createWebserviceCallback(it))
            }
        }
    }
}