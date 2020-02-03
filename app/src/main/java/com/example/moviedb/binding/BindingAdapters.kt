package com.example.moviedb.binding

import android.util.Log
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.moviedb.model.discover.DiscoverResult
import com.squareup.picasso.Picasso

@BindingAdapter("loadImg")
fun loadImg(view:ImageView, path:String?){
    path?.let {
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500$it")
            .into(view)
    }
}

@BindingAdapter("calculateProgress")
fun calculateProgress(view:ProgressBar, data:DiscoverResult?){
    data?.let {
        val res = data.voteAverage / 10 * 100
        view.progress = res.toInt()
    }
}

@BindingAdapter("showProgress")
fun showProgress(view: TextView, data: DiscoverResult?){
    data?.let {
        val res = data.voteAverage / 10 * 100
        view.text = "${res.toInt()}%"
    }
}
