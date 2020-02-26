package com.example.moviedb.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.ui.base.BaseMovieAdapter
import com.example.moviedb.ui.home.adapter.viewHolders.NetworkSrateHolder
import com.example.moviedb.ui.home.adapter.viewHolders.TvItemHolder
import java.lang.IllegalStateException

class TvRvAdapter: BaseMovieAdapter<ResultTV>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.head_film_item -> TvItemHolder.create(parent, listener)
            R.layout.network_state_head_item -> NetworkSrateHolder.create(parent)
            else -> throw IllegalStateException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            R.layout.head_film_item -> (holder as TvItemHolder).bind(getItem(position))
            R.layout.network_state_head_item -> (holder as NetworkSrateHolder).bind(super.networkState)
        }
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