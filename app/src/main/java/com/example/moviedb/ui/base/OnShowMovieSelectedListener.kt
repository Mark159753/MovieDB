package com.example.moviedb.ui.base

import android.view.View

interface OnShowMovieSelectedListener {

    companion object{
        const val MOVIE_TYPE = 1
        const val TV_SHOW_TYPE = 2
        const val PERSON_TYPE = 3

        const val CONTENT_ID = "com.example.moviedb.ui.base.CONTENT_ID"
    }

    fun onItemSelected(view: View, id:Int, contentType:Int)
}