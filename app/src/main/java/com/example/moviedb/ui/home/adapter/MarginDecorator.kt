package com.example.moviedb.ui.home.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MarginDecorator(
    private val left:Int,
    private val right:Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        if (position == 0){
            outRect.left = left *2
            outRect.right = right
        }else {
            outRect.left = left
            outRect.right = right
        }
    }
}