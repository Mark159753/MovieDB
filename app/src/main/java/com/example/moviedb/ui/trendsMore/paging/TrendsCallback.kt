package com.example.moviedb.ui.trendsMore.paging

import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.model.theathre.InTheaterResponse
import com.example.moviedb.model.trends.TrendResult
import com.example.moviedb.model.trends.TrendsResponse
import com.example.moviedb.ui.base.BasePagingCallback
import com.example.moviedb.ui.base.PagingRequestHelper
import java.util.concurrent.Executor

class TrendsCallback(
    private val apiServer: TMDBserver,
    private val ioExecutor: Executor,
    handlerResponse: (TrendsResponse?) -> Unit,
    private val hadleNextPage: (mediaType:String) -> Int,
    private val language:String,
    private val mediaType:String
): BasePagingCallback<TrendResult, TrendsResponse>(ioExecutor, handlerResponse)  {

    override fun onZeroItemsLoaded() {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.INITIAL){
            apiServer.getMovieTrends(media_type = mediaType, language = language)
                .enqueue(createWebserviceCallback(it))
        }
    }

    override fun onItemAtEndLoaded(itemAtEnd: TrendResult) {
        helper.runIfNotRunning(PagingRequestHelper.RequestType.AFTER){
            ioExecutor.execute {
                val page = hadleNextPage.invoke(mediaType)
                apiServer.getMovieTrends(media_type = mediaType, language = language, page = page)
                    .enqueue(createWebserviceCallback(it))
            }
        }
    }
}