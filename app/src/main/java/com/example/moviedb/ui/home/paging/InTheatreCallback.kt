package com.example.moviedb.ui.home.paging

import android.content.Context
import android.util.Log
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.theathre.InTheaterResponse
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.ui.base.BasePagingCallback
import com.example.moviedb.ui.base.PagingRequestHelper
import com.example.moviedb.until.LocaleHelper
import java.util.concurrent.Executor

class InTheatreCallback(
    private val apiServer:TMDBserver,
    private val ioExecutor: Executor,
    private val handlerResponse: (InTheaterResponse?) -> Unit,
    private val hadleNextPage: () -> Int,
    private val context: Context
): BasePagingCallback<ResultMovie, InTheaterResponse>(ioExecutor, handlerResponse) {


    override fun onZeroItemsLoaded() {
        val lan = LocaleHelper.getLanguage(context)
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
            apiServer.getInTheatre(lan)
                .enqueue(createWebserviceCallback(it))
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: ResultMovie) {
        val lan = LocaleHelper.getLanguage(context)
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            ioExecutor.execute {
                val page = hadleNextPage.invoke()
                apiServer.getInTheatre(lan, page)
                    .enqueue(createWebserviceCallback(it))
            }
        }
    }


}