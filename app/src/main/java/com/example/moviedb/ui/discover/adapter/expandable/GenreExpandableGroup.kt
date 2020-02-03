package com.example.moviedb.ui.discover.adapter.expandable

import com.example.moviedb.model.genre.Genre
import com.thoughtbot.expandablecheckrecyclerview.models.MultiCheckExpandableGroup

class GenreExpandableGroup(
    title:String,
    items:List<Genre>
): MultiCheckExpandableGroup(title, items) {

}