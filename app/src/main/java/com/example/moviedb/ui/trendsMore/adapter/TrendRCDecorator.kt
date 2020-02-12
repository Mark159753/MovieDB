package com.example.moviedb.ui.trendsMore.adapter

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class TrendRCDecorator(
    private val leftRight:Int,
    private val topBottom:Int
): RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        outRect.top = topBottom
        outRect.bottom = topBottom
        outRect.left = leftRight
        outRect.right = leftRight
    }
}