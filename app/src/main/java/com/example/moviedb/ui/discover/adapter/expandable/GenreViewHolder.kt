package com.example.moviedb.ui.discover.adapter.expandable

import android.view.View
import android.widget.CheckBox
import android.widget.Checkable
import android.widget.CheckedTextView
import com.example.moviedb.R
import com.example.moviedb.model.genre.Genre
import com.thoughtbot.expandablecheckrecyclerview.viewholders.CheckableChildViewHolder

class GenreViewHolder(view:View): CheckableChildViewHolder(view){
    val genreTitle = view.findViewById<CheckedTextView>(R.id.expandedListItem)

    override fun getCheckable(): Checkable {
        return genreTitle
    }

    fun onBind(genre:Genre){
        genreTitle.text = genre.name
    }
}