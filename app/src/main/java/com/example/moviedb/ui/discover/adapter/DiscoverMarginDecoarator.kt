package com.example.moviedb.ui.discover.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DiscoverMarginDecoarator(
    private val left:Int,
    private val right:Int,
    private val bottom_top:Int
) :RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildLayoutPosition(view)
        outRect.right = right
        outRect.left = left
        outRect.bottom = bottom_top
        if (position == 0) {
            outRect.top = bottom_top / 2
        }else{
            outRect.top = bottom_top
        }
    }
}