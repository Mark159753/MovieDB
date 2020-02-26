package com.example.moviedb.ui.base

import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.until.NetworkState

abstract class BaseMovieAdapter<T>(callback:DiffUtil.ItemCallback<T>)
    :PagedListAdapter<T, RecyclerView.ViewHolder>(callback){

    internal var networkState: NetworkState? = null
    internal var listener:OnShowMovieSelectedListener? = null

    fun setListener(listener:OnShowMovieSelectedListener){
        this.listener = listener
    }

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

    protected fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

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

}