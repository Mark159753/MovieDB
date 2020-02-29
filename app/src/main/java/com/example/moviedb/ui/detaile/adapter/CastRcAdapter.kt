package com.example.moviedb.ui.detaile.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.HeadFilmItemBinding
import com.example.moviedb.model.cast.Cast
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.example.moviedb.until.NetworkState
import com.squareup.picasso.Picasso

class CastRcAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var listOfData = emptyList<Cast>()
    private var networkState: NetworkState? = null
    private var listener:OnShowMovieSelectedListener? = null

    fun setListener(listener:OnShowMovieSelectedListener){
        this.listener = listener
    }

    fun setList(list: List<Cast>){
        this.listOfData = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            R.layout.head_film_item -> CastViewHolder.create(parent, listener)
            R.layout.network_state_head_item -> CastNetworkSateHolder.create(parent)
            else -> throw IllegalStateException("unknown view type $viewType")
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(getItemViewType(position)){
            R.layout.head_film_item -> (holder as CastViewHolder).bind(listOfData[position])
            R.layout.network_state_head_item -> (holder as CastNetworkSateHolder).bind(networkState)
        }
    }

    fun setNetworkState(newNetworkState: NetworkState?) {
        val previousState = this.networkState
        val hadExtraRow = hasExtraRow()
        this.networkState = newNetworkState
        val hasExtraRow = hasExtraRow()
        if (hadExtraRow != hasExtraRow) {
            if (hadExtraRow) {
                notifyItemRemoved(listOfData.size)
            } else {
                notifyItemInserted(listOfData.size)
            }
        } else if (hasExtraRow && previousState != newNetworkState) {
            notifyItemChanged(itemCount - 1)
        }
    }

    private fun hasExtraRow() = networkState != null && networkState != NetworkState.LOADED

    override fun getItemCount(): Int {
        return listOfData.size + if (hasExtraRow()) 1 else 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (hasExtraRow() && position == itemCount -1){
            R.layout.network_state_head_item
        }else{
            R.layout.head_film_item
        }
    }

    private class CastViewHolder(view: View, private val listener:OnShowMovieSelectedListener?): RecyclerView.ViewHolder(view) {

        private var binder: HeadFilmItemBinding = DataBindingUtil.bind(view)!!

        fun bind(data: Cast?){
            binder.movieTitle.text = data!!.name
            binder.root.setOnClickListener {
                listener?.onItemSelected(it, data.id, OnShowMovieSelectedListener.PERSON_TYPE)
            }
            Picasso.get()
                .load("https://image.tmdb.org/t/p/w500" + data.profilePath)
                .into(binder.headPosterImg)
            ViewCompat.setTransitionName(binder.headPosterImg, "headPoster${data.id}")
            ViewCompat.setTransitionName(binder.movieTitle, "headTitle${data.id}")
        }


        companion object{
            fun create(parent:ViewGroup, listener:OnShowMovieSelectedListener?):CastViewHolder{
                val inflater = LayoutInflater.from(parent.context)
                val binding = HeadFilmItemBinding.inflate(inflater, parent, false)
                return CastViewHolder(binding.root, listener)
            }
        }
    }

    private class CastNetworkSateHolder(view:View) :RecyclerView.ViewHolder(view){
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
            fun create(parent:ViewGroup):CastNetworkSateHolder{
                val view = LayoutInflater.from(parent.context).inflate(R.layout.network_state_head_item, parent, false)
                return CastNetworkSateHolder(view)
            }
        }
    }
}