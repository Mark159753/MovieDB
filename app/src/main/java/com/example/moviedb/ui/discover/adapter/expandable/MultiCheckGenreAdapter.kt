package com.example.moviedb.ui.discover.adapter.expandable

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.moviedb.R
import com.example.moviedb.model.genre.Genre
import com.thoughtbot.expandablecheckrecyclerview.CheckableChildRecyclerViewAdapter
import com.thoughtbot.expandablecheckrecyclerview.models.CheckedExpandableGroup
import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup

class MultiCheckGenreAdapter(
    dataList:List<GenreExpandableGroup>
): CheckableChildRecyclerViewAdapter<GenreTitleViewHolder, GenreViewHolder>(dataList) {

    override fun onBindCheckChildViewHolder(
        holder: GenreViewHolder?,
        flatPosition: Int,
        group: CheckedExpandableGroup?,
        childIndex: Int
    ) {
        val item = group!!.items[childIndex] as Genre
        holder!!.onBind(item)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): GenreTitleViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_group, parent, false)
        return GenreTitleViewHolder(view)
    }

    override fun onCreateCheckChildViewHolder(parent: ViewGroup?, viewType: Int): GenreViewHolder {
        val view = LayoutInflater.from(parent!!.context).inflate(R.layout.list_item, parent, false)
        return GenreViewHolder(view)
    }

    override fun onBindGroupViewHolder(
        holder: GenreTitleViewHolder?,
        flatPosition: Int,
        group: ExpandableGroup<*>?
    ) {
        holder!!.setGenreTitle(group!!.title)
    }
}