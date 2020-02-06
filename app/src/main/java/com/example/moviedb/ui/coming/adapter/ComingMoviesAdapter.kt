package com.example.moviedb.ui.coming.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.model.genre.Genre
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.ui.base.BaseMovieAdapter
import com.example.moviedb.ui.coming.adapter.viewholders.ComingItemViewHolder
import com.example.moviedb.ui.coming.adapter.viewholders.ComingNetworkSateViewHolder
import java.lang.IllegalStateException

class ComingMoviesAdapter : BaseMovieAdapter<ResultMovie>(COMPARATOR) {

    private var genresList:List<Genre>? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.coming_item -> ComingItemViewHolder.create(parent)
            R.layout.coming_network_item -> ComingNetworkSateViewHolder(parent)
            else -> throw IllegalStateException("unknown viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            R.layout.coming_item -> (holder as ComingItemViewHolder).bind(getItem(position), genresList)
            R.layout.coming_network_item -> (holder as ComingNetworkSateViewHolder).bind(networkState)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount -1){
            R.layout.coming_network_item
        }else{
            R.layout.coming_item
        }
    }

    fun setGenres(list:List<Genre>){
        genresList = list
        notifyDataSetChanged()
    }

    companion object{
        val COMPARATOR = object :DiffUtil.ItemCallback<ResultMovie>(){
            override fun areItemsTheSame(oldItem: ResultMovie, newItem: ResultMovie): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ResultMovie, newItem: ResultMovie): Boolean {
                return oldItem == newItem
            }
        }
    }
}