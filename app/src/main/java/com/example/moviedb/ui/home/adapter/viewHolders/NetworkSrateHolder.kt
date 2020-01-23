package com.example.moviedb.ui.home.adapter.viewHolders

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviedb.R
import com.example.moviedb.until.NetworkState

class NetworkSrateHolder(view:View) :RecyclerView.ViewHolder(view){
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
        fun create(parent:ViewGroup):NetworkSrateHolder{
            val view = LayoutInflater.from(parent.context).inflate(R.layout.network_state_head_item, parent, false)
            return NetworkSrateHolder(view)
        }
    }
}