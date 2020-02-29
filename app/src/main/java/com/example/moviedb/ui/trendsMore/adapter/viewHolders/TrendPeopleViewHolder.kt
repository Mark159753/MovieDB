package com.example.moviedb.ui.trendsMore.adapter.viewHolders

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.model.trends.TrendResult
import com.squareup.picasso.Picasso

class TrendPeopleViewHolder(private val context: Context, view:View):RecyclerView.ViewHolder(view) {

    private val poster: ImageView = view.findViewById(R.id.trend_list_poster)
    private val title: TextView = view.findViewById(R.id.trend_list_title)
    private val genres: TextView = view.findViewById(R.id.trend_list_genres)

    fun bind(data: TrendResult?){
        title.text = data?.name
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + data?.profilePath)
            .into(poster)
        poster.clipToOutline = true
        genres.text = if (data?.gender == 1)
            context.resources.getString(R.string.gender_woman)
            else context.resources.getString(R.string.gender_man)
    }

    companion object{
        fun create(context: Context, parent: ViewGroup):TrendPeopleViewHolder{
            val v = LayoutInflater.from(parent.context).inflate(R.layout.trend_item, parent, false)
            return TrendPeopleViewHolder(context, v)
        }
    }
}