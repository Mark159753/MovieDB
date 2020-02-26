package com.example.moviedb.ui.detaile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.HeadFilmItemBinding
import com.example.moviedb.model.similarMovies.Result
import com.example.moviedb.ui.base.BaseMovieAdapter
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.example.moviedb.until.NetworkState
import com.squareup.picasso.Picasso

class SimilarRCAdapter:BaseMovieAdapter<Result>(COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.head_film_item -> MovieViewHolder.create(parent, listener)
            R.layout.network_state_head_item -> NetworkStateViewHolder.create(parent)
            else -> throw IllegalStateException("unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            R.layout.head_film_item -> (holder as MovieViewHolder).bind(getItem(position))
            R.layout.network_state_head_item -> (holder as NetworkStateViewHolder).bind(super.networkState)
        }
    }


    private class MovieViewHolder(view:View, private val listener: OnShowMovieSelectedListener?):RecyclerView.ViewHolder(view){

        private var binder:HeadFilmItemBinding = DataBindingUtil.bind(view)!!

        fun bind(data: Result?){
            binder.movieTitle.text = data!!.title
            binder.root.setOnClickListener {
                listener?.onItemSelected(it, data.id, OnShowMovieSelectedListener.MOVIE_TYPE)
            }
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + data.posterPath)
                .into(binder.headPosterImg)
//            ViewCompat.setTransitionName(binder.headPosterImg, "headPoster${data.id}")
//            ViewCompat.setTransitionName(binder.movieTitle, "headTitle${data.id}")
        }

        companion object{
            fun create(parent:ViewGroup, listener:OnShowMovieSelectedListener?):MovieViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = HeadFilmItemBinding.inflate(inflater, parent, false)
                return MovieViewHolder(binding.root, listener)
            }
        }
    }

    private class NetworkStateViewHolder(view: View):RecyclerView.ViewHolder(view){
        private val progress = view.findViewById<ProgressBar>(R.id.loading_item_progress)
        private val errorMessage = view.findViewById<TextView>(R.id.message_error)

        fun bind(networkState: NetworkState?){
            progress.visibility = toVisibility(networkState is NetworkState.LOADING)
            errorMessage.visibility = toVisibility(networkState is NetworkState.ERROR)
            errorMessage.text = if (networkState is NetworkState.ERROR) networkState.msg else ""
        }

        private fun toVisibility(constraint : Boolean): Int {
            return if (constraint) {
                View.VISIBLE
            } else {
                View.GONE
            }
        }

        companion object{
            fun create(parent:ViewGroup):NetworkStateViewHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.network_state_head_item, parent, false)
                return NetworkStateViewHolder(view)
            }
        }
    }

    companion object{
        val COMPARATOR = object :DiffUtil.ItemCallback<Result>(){
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
        }
    }
}