package com.example.moviedb.ui.home

import android.app.Activity
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.example.moviedb.ui.detaile.MovieDetailActivity

class TrendMovieListener(
    private val activity:Activity,
    private val img:ImageView,
    private val text:TextView
):OnShowMovieSelectedListener {
    override fun onItemSelected(view: View, id: Int, contentType: Int) {
        when(contentType){
            OnShowMovieSelectedListener.MOVIE_TYPE -> {
                val option = ActivityOptionsCompat.makeSceneTransitionAnimation(activity,
                    Pair(img, "headPoster$id"),
                    Pair(text, "headTitle$id"),
                    Pair(text, "headTitle$id")
                )
                val intent = Intent(activity, MovieDetailActivity::class.java)
                intent.putExtra(OnShowMovieSelectedListener.CONTENT_ID, id)
                activity.startActivity(intent, option.toBundle())
            }
        }
    }
}