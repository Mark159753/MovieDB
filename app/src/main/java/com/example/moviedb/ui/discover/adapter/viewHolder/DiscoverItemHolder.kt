package com.example.moviedb.ui.discover.adapter.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.databinding.DiscoverCardItemBinding
import com.example.moviedb.model.discover.DiscoverResult
import com.example.moviedb.ui.base.OnShowMovieSelectedListener

class DiscoverItemHolder(view:View, private val listener:OnShowMovieSelectedListener?):RecyclerView.ViewHolder(view) {

    private val binder:DiscoverCardItemBinding = DataBindingUtil.bind(view)!!

    fun bind(data:DiscoverResult?){
        binder.data = data
        ViewCompat.setTransitionName(binder.discoverPoster, "headPoster${data?.id}")
        ViewCompat.setTransitionName(binder.discoverTitle, "headTitle${data?.id}")
        binder.root.setOnClickListener {
            listener?.onItemSelected(it, data?.id ?: 0, OnShowMovieSelectedListener.MOVIE_TYPE)
        }
    }

    companion object{
        fun create(parent: ViewGroup, listener: OnShowMovieSelectedListener?): DiscoverItemHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DiscoverCardItemBinding.inflate(inflater, parent, false)
            return DiscoverItemHolder(binding.root, listener)
        }
    }
}