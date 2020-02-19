package com.example.moviedb.ui.home.paging

import android.content.Context
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.tv.OnTvResponse
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.ui.base.BasePagingCallback
import com.example.moviedb.ui.base.PagingRequestHelper
import com.example.moviedb.until.LocaleHelper
import java.util.concurrent.Executor

class OnTvCallback(
    private val apiServer: TMDBserver,
    private val ioExecutor: Executor,
    private val handlerResponse: (OnTvResponse?) -> Unit,
    private val hadleNextPage: () -> Int,
    private val context: Context
): BasePagingCallback<ResultTV, OnTvResponse>(ioExecutor, handlerResponse) {

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
            apiServer.getOnTv(LocaleHelper.getLanguage(context))
                .enqueue(createWebserviceCallback(it))
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: ResultTV) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            ioExecutor.execute {
                val page = hadleNextPage.invoke()
                apiServer.getOnTv(LocaleHelper.getLanguage(context), page)
                    .enqueue(createWebserviceCallback(it))
            }
        }
    }
}