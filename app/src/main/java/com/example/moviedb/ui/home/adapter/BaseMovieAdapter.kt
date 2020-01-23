package com.example.moviedb.ui.home.adapter

import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.ui.home.adapter.viewHolders.HeadItemHolder
import com.example.moviedb.ui.home.adapter.viewHolders.NetworkSrateHolder
import com.example.moviedb.until.NetworkState
import java.lang.IllegalStateException

abstract class BaseMovieAdapter<T>(callback:DiffUtil.ItemCallback<T>)
    :PagedListAdapter<T, RecyclerView.ViewHolder>(callback){

    internal var networkState: NetworkState? = null

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(super.getItemCount())
            } else {
                notifyItemInserted(super.getItemCount())
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount(): Int {
        return super.getItemCount() + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount -1){
            R.layout.network_state_head_item
        }else{
            R.layout.head_film_item
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.head_film_item -> HeadItemHolder.create(parent)
            R.layout.network_state_head_item -> NetworkSrateHolder.create(parent)
            else -> throw IllegalStateException("unknown view type $viewType")
        }
    }

}