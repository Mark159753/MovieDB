package com.example.moviedb.ui.discover.adapter.expandable

import android.view.View
import android.view.animation.Animation.RELATIVE_TO_SELF
import android.view.animation.RotateAnimation
import android.widget.ImageView
import android.widget.TextView
import com.example.moviedb.R
import com.thoughtbot.expandablerecyclerview.viewholders.GroupViewHolder


class GenreTitleViewHolder(view:View):GroupViewHolder(view) {

    private val title = view.findViewById<TextView>(R.id.listTitle)
    private val arrow = view.findViewById<ImageView>(R.id.list_item_genre_arrow)

    fun setGenreTitle(group: String){
        title.text = group
    }

    override fun expand() {
        animateExpand()
    }

    override fun collapse() {
       animateCollapse()
    }

    private fun animateExpand() {
        val rotate = RotateAnimation(
            360f,
            180f,
            RELATIVE_TO_SELF,
            0.5f,
            RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }

    private fun animateCollapse() {
        val rotate = RotateAnimation(
            180f,
            360f,
            RELATIVE_TO_SELF,
            0.5f,
            RELATIVE_TO_SELF,
            0.5f
        )
        rotate.duration = 300
        rotate.fillAfter = true
        arrow.animation = rotate
    }
}