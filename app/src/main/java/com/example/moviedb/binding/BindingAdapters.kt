package com.example.moviedb.binding

import android.util.Log
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.squareup.picasso.Picasso

@BindingAdapter("loadImg")
fun loadImg(view:ImageView, path:String?){
    path?.let {
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500$it")
            .into(view)
    }
}