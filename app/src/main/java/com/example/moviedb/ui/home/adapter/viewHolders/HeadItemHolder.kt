package com.example.moviedb.ui.home.adapter.viewHolders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.databinding.HeadFilmItemBinding
import com.example.moviedb.model.theathre.ResultMovie
import com.squareup.picasso.Picasso

class HeadItemHolder(view:View): RecyclerView.ViewHolder(view) {

    private var binder:HeadFilmItemBinding = DataBindingUtil.bind(view)!!

    fun bind(data: ResultMovie?){
        binder.movieTitle.text = data!!.title
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + data.posterPath)
            .into(binder.headPosterImg)
    }


    companion object{
        fun create(parent:ViewGroup):HeadItemHolder{
            val inflater = LayoutInflater.from(parent.context)
            val binding = HeadFilmItemBinding.inflate(inflater, parent, false)
            return HeadItemHolder(binding.root)
        }
    }
}