package com.example.moviedb.ui.home.adapter.viewHolders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.databinding.HeadFilmItemBinding
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.squareup.picasso.Picasso

class TvItemHolder(view:View, private val listener: OnShowMovieSelectedListener?):RecyclerView.ViewHolder(view) {

    private var binder: HeadFilmItemBinding = DataBindingUtil.bind(view)!!

    fun bind(data: ResultTV?){
        data?.let {
            binder.movieTitle.text = it.name
            binder.root.setOnClickListener {v ->
                listener?.onItemSelected(v, it.id, OnShowMovieSelectedListener.TV_SHOW_TYPE)
            }
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + it.posterPath)
                .into(binder.headPosterImg)
        }
    }

    companion object{
        fun create(parent: ViewGroup, listener:OnShowMovieSelectedListener?):TvItemHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = HeadFilmItemBinding.inflate(inflater, parent, false)
            return TvItemHolder(binding.root, listener)
        }
    }
}