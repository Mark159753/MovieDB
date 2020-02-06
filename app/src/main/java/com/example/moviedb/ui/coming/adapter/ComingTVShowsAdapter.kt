package com.example.moviedb.ui.coming.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.model.genre.Genre
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.ui.base.BaseMovieAdapter
import com.example.moviedb.ui.coming.adapter.viewholders.ComingNetworkSateViewHolder
import com.example.moviedb.ui.coming.adapter.viewholders.ComingTvShowsViewHolder
import java.lang.IllegalStateException

class ComingTVShowsAdapter:BaseMovieAdapter<ResultTV>(COMPARATOR) {

    private var genresList:List<Genre>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.coming_item -> ComingTvShowsViewHolder.create(parent)
            R.layout.coming_network_item -> ComingNetworkSateViewHolder.create(parent)
            else -> throw IllegalStateException("unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            R.layout.coming_item -> (holder as ComingTvShowsViewHolder).bind(getItem(position), genresList)
            R.layout.coming_network_item -> (holder as ComingNetworkSateViewHolder).bind(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount -1){
            R.layout.coming_network_item
        }else{
            R.layout.coming_item
        }
    }

    fun setGenres(list:List<Genre>){
        genresList = list
        notifyDataSetChanged()
    }

    companion object{
        val COMPARATOR = object :DiffUtil.ItemCallback<ResultTV>(){
            override fun areItemsTheSame(oldItem: ResultTV, newItem: ResultTV): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResultTV, newItem: ResultTV): Boolean {
                return oldItem == newItem
            }
        }
    }
}