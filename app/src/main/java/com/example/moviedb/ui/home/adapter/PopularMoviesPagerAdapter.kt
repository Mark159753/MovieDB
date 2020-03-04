package com.example.moviedb.ui.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.asksira.loopingviewpager.LoopingPagerAdapter
import com.example.moviedb.R
import com.example.moviedb.databinding.PopularMovieCardBinding
import com.example.moviedb.model.popular.PopularResult
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.squareup.picasso.Picasso

class PopularMoviesPagerAdapter(
    context: Context,
    private val listItems:List<PopularResult>,
    isInfinite:Boolean
):LoopingPagerAdapter<PopularResult>(context, listItems, isInfinite) {

    private lateinit var binder:PopularMovieCardBinding
    private var listener:OnShowMovieSelectedListener? = null

    fun setListener(listener:OnShowMovieSelectedListener){
        this.listener = listener
    }

    override fun inflateView(viewType: Int, container: ViewGroup?, listPosition: Int): View {
        binder = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.popular_movie_card, container, false)
        return binder.root
    }

    override fun bindView(convertView: View?, position: Int, viewType: Int) {
        val item = listItems[position]
        binder.popularTitle.text = item.title
        binder.popularOverview.text = item.overview
        binder.popularRating.text = item.popularity.toString()
        binder.popularStars.rating = ((item.popularity / 1000f)* 10f).toFloat()
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + item.posterPath)
            .into(binder.popularPoster)

        ViewCompat.setTransitionName(binder.popularPoster, "headPoster${item.id}")
        ViewCompat.setTransitionName(binder.popularTitle, "headTitle${item.id}")

        binder.root.setOnClickListener {
            listener?.onItemSelected(it, item.id, OnShowMovieSelectedListener.MOVIE_TYPE)
        }
    }
}