package com.example.moviedb.ui.person.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.databinding.HeadFilmItemBinding
import com.example.moviedb.model.person.credits.KnownForCast
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.squareup.picasso.Picasso
import java.lang.IllegalStateException

class KnownForRCAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list = emptyList<KnownForCast>()
    private var listener:OnShowMovieSelectedListener? = null

    fun setListener(listener: OnShowMovieSelectedListener){
        this.listener = listener
    }

    fun setDataList(list: List<KnownForCast>){
        this.list = list
        notifyDataSetChanged()
    }

    companion object{
        private const val MOVIE_TYPE = 1
        private const val TV_TYPE = 2
    }

    override fun getItemViewType(position: Int): Int {
        return if (list[position].mediaType.equals("movie", true))
                MOVIE_TYPE
            else
                TV_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            MOVIE_TYPE -> MovieViewHolder.create(parent, listener)
            TV_TYPE -> TVViewHolder.create(parent, listener)
            else -> throw IllegalStateException("Unknown ViewType")
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            MOVIE_TYPE -> (holder as MovieViewHolder).bind(list[position])
            TV_TYPE -> (holder as TVViewHolder).bind(list[position])
        }
    }

    private class MovieViewHolder(view:View, private val listener: OnShowMovieSelectedListener?):RecyclerView.ViewHolder(view){
        private var binder: HeadFilmItemBinding = DataBindingUtil.bind(view)!!

        fun bind(data: KnownForCast?){
            binder.movieTitle.text = data!!.title
            binder.root.setOnClickListener {
                listener?.onItemSelected(it, data.id, OnShowMovieSelectedListener.MOVIE_TYPE)
            }
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + data.posterPath)
                .into(binder.headPosterImg)
            ViewCompat.setTransitionName(binder.headPosterImg, "headPoster${data.id}")
            ViewCompat.setTransitionName(binder.movieTitle, "headTitle${data.id}")
        }


        companion object{
            fun create(parent:ViewGroup, listener:OnShowMovieSelectedListener?):MovieViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = HeadFilmItemBinding.inflate(inflater, parent, false)
                return MovieViewHolder(binding.root, listener)
            }
        }
    }

    private class TVViewHolder(view:View, private val listener: OnShowMovieSelectedListener?):RecyclerView.ViewHolder(view){
        private var binder: HeadFilmItemBinding = DataBindingUtil.bind(view)!!

        fun bind(data: KnownForCast?){
            binder.movieTitle.text = data!!.name
            binder.root.setOnClickListener {
                listener?.onItemSelected(it, data.id, OnShowMovieSelectedListener.TV_SHOW_TYPE)
            }
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + data.posterPath)
                .into(binder.headPosterImg)
            ViewCompat.setTransitionName(binder.headPosterImg, "headPoster${data.id}")
            ViewCompat.setTransitionName(binder.movieTitle, "headTitle${data.id}")
        }


        companion object{
            fun create(parent:ViewGroup, listener:OnShowMovieSelectedListener?):TVViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = HeadFilmItemBinding.inflate(inflater, parent, false)
                return TVViewHolder(binding.root, listener)
            }
        }
    }
}