package com.example.moviedb.ui.coming.adapter.viewholders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.databinding.ComingItemBinding
import com.example.moviedb.model.genre.Genre
import com.example.moviedb.model.theathre.ResultMovie
import com.squareup.picasso.Picasso

class ComingItemViewHolder(view:View): RecyclerView.ViewHolder(view) {

    private val binder:ComingItemBinding = DataBindingUtil.bind(view)!!


    fun bind(data:ResultMovie?, genres:List<Genre>?){
        binder.comingTitle.text = data?.title
        binder.comingReleaseDate.text = data?.releaseDate
        binder.comingOverview.text = data?.overview
        Picasso.get()
            .load("https://image.tmdb.org/t/p/w500" + data?.posterPath)
            .into(binder.comingPoster)
        data?.genreIds?.let {
            try { bindGenre(binder.comingGenre1, it[0], genres) }catch (e:IndexOutOfBoundsException){}
            try { bindGenre(binder.comingGenre2, it[1], genres) }catch (e:IndexOutOfBoundsException){}
            try { bindGenre(binder.comingGenre3, it[2], genres) }catch (e:IndexOutOfBoundsException){}
        }
    }

    private fun bindGenre(textView: TextView, genre:Int, genresList:List<Genre>?){
        genresList?.let {
            for (i in it){
                if (i.id == genre){
                    textView.visibility = View.VISIBLE
                    textView.text = i.name
                    break
                }
            }
        }

    }

    companion object{
        fun create(parent: ViewGroup): ComingItemViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ComingItemBinding.inflate(inflater, parent, false)
            return ComingItemViewHolder(binding.root)
        }
    }
}