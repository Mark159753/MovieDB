package com.example.moviedb.ui.seasons.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.databinding.EpisodeItemBinding
import com.example.moviedb.model.seasons.Episode
import com.example.moviedb.ui.base.OnShowMovieSelectedListener

class EpisodeListAdapterRC : RecyclerView.Adapter<EpisodeListAdapterRC.Holder>() {

    private var listOfDate = emptyList<Episode>()
    private var listener:OnShowMovieSelectedListener? = null

    fun setListener(listener: OnShowMovieSelectedListener){
        this.listener = listener
    }

    fun setLisOfData(list: List<Episode>){
        this.listOfDate = list
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder.create(parent)
    }

    override fun getItemCount(): Int {
        return listOfDate.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(listOfDate[position])
    }

    class Holder(view:View):RecyclerView.ViewHolder(view){
        private var binder:EpisodeItemBinding = DataBindingUtil.bind(view)!!

        fun bind(data:Episode?){
            data?.let { binder.episode = it }
        }

        companion object{
            fun create(parent:ViewGroup): Holder {
                val inflater = LayoutInflater.from(parent.context)
                val binding = EpisodeItemBinding.inflate(inflater, parent, false)
                return Holder(binding.root)
            }
        }
    }
}