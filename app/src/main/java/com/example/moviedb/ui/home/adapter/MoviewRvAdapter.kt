package com.example.moviedb.ui.home.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.ui.base.BaseMovieAdapter
import com.example.moviedb.ui.home.adapter.viewHolders.HeadItemHolder
import com.example.moviedb.ui.home.adapter.viewHolders.NetworkSrateHolder
import java.lang.IllegalStateException


class MovieRvAdapter : BaseMovieAdapter<ResultMovie>(COMPERATOR){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.head_film_item -> HeadItemHolder.create(parent, listener)
            R.layout.network_state_head_item -> NetworkSrateHolder.create(parent)
            else -> throw IllegalStateException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            R.layout.head_film_item -> (holder as HeadItemHolder).bind(getItem(position))
            R.layout.network_state_head_item -> (holder as NetworkSrateHolder).bind(super.networkState)
        }
    }

    companion object{
        val COMPERATOR = object :DiffUtil.ItemCallback<ResultMovie>(){
            override fun areItemsTheSame(oldItem: ResultMovie, newItem: ResultMovie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResultMovie, newItem: ResultMovie): Boolean {
                return oldItem == newItem
            }
        }
    }
}