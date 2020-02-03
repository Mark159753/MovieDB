package com.example.moviedb.ui.discover.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.model.discover.DiscoverResult
import com.example.moviedb.ui.base.BaseMovieAdapter
import com.example.moviedb.ui.discover.adapter.viewHolder.DiscoverItemHolder
import com.example.moviedb.ui.discover.adapter.viewHolder.DiscoverLoadHolder
import java.lang.IllegalStateException

class DiscoverListAdapter:BaseMovieAdapter<DiscoverResult>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.discover_card_item -> {DiscoverItemHolder.create(parent)}
            R.layout.discover_load_item -> {DiscoverLoadHolder.create(parent)}
            else -> throw IllegalStateException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            R.layout.discover_card_item -> {(holder as DiscoverItemHolder).bind(getItem(position))}
            R.layout.discover_load_item -> {(holder as DiscoverLoadHolder).bind(networkState)}
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount -1){
            R.layout.discover_load_item
        }else{
            R.layout.discover_card_item
        }
    }

    companion object{
        val COMPARATOR = object :DiffUtil.ItemCallback<DiscoverResult>(){
            override fun areItemsTheSame(
                oldItem: DiscoverResult,
                newItem: DiscoverResult
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: DiscoverResult,
                newItem: DiscoverResult
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}