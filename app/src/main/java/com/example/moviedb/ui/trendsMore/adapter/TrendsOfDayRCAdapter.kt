package com.example.moviedb.ui.trendsMore.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.data.repository.trends.TrendsRepository
import com.example.moviedb.model.genre.Genre
import com.example.moviedb.model.trends.TrendResult
import com.example.moviedb.ui.base.BaseMovieAdapter
import com.example.moviedb.ui.trendsMore.adapter.viewHolders.TrendPeopleViewHolder
import com.example.moviedb.ui.trendsMore.adapter.viewHolders.TrendTVViewHolder
import com.example.moviedb.ui.trendsMore.adapter.viewHolders.TrendsLoadVeiwHolder
import com.example.moviedb.ui.trendsMore.adapter.viewHolders.TrendsMovieViewHolder
import java.lang.IllegalStateException

private const val MOVIE_VIEW_TYPE = 1
private const val PEOPLE_VIEW_TYPE = 2
private const val NETWORK_LOAD_VIEW_TYPE = 3
private const val TV_VIEW_TYPE = 4

class TrendsOfDayRCAdapter(private val context: Context): BaseMovieAdapter<TrendResult>(COMPARATOR) {

    private var genresList:List<Genre>? = null

    fun setGenres(list:List<Genre>){
        genresList = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            MOVIE_VIEW_TYPE -> TrendsMovieViewHolder.create(parent)
            NETWORK_LOAD_VIEW_TYPE -> TrendsLoadVeiwHolder.create(parent)
            PEOPLE_VIEW_TYPE -> TrendPeopleViewHolder.create(context, parent)
            TV_VIEW_TYPE -> TrendTVViewHolder.create(parent)
            else -> throw IllegalStateException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            NETWORK_LOAD_VIEW_TYPE -> (holder as TrendsLoadVeiwHolder).bind(networkState)
            MOVIE_VIEW_TYPE -> (holder as TrendsMovieViewHolder).bind(getItem(position), genresList)
            PEOPLE_VIEW_TYPE -> (holder as TrendPeopleViewHolder).bind(getItem(position))
            TV_VIEW_TYPE -> (holder as TrendTVViewHolder).bind(getItem(position), genresList)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount -1){
            NETWORK_LOAD_VIEW_TYPE
        }else if(getItem(position)?.mediaType == TrendsRepository.TREND_PERSON_TYPE){
            PEOPLE_VIEW_TYPE
        }else if(getItem(position)?.mediaType == TrendsRepository.TREND_TV_SHOW_TYPE) {
            TV_VIEW_TYPE
        }else
            MOVIE_VIEW_TYPE
    }

    companion object{
        val COMPARATOR = object : DiffUtil.ItemCallback<TrendResult>(){
            override fun areItemsTheSame(oldItem: TrendResult, newItem: TrendResult): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: TrendResult, newItem: TrendResult): Boolean {
                return oldItem == newItem
            }
        }
    }
}