package com.example.moviedb.ui.discover.adapter.viewHolder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.databinding.DiscoverCardItemBinding
import com.example.moviedb.model.discover.DiscoverResult

class DiscoverItemHolder(view:View):RecyclerView.ViewHolder(view) {

    private val binder:DiscoverCardItemBinding = DataBindingUtil.bind(view)!!

    fun bind(data:DiscoverResult?){
        binder.data = data
    }

    companion object{
        fun create(parent: ViewGroup): DiscoverItemHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = DiscoverCardItemBinding.inflate(inflater, parent, false)
            return DiscoverItemHolder(binding.root)
        }
    }
}