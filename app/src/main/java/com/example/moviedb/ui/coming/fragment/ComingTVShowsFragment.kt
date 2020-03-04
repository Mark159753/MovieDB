package com.example.moviedb.ui.coming.fragment

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.moviedb.MovieApp

import com.example.moviedb.R
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.ui.MainActivity
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.example.moviedb.ui.coming.adapter.ComingTVShowsAdapter
import com.example.moviedb.ui.tvDetails.TvDetailsActivity
import kotlinx.android.synthetic.main.coming_tvshows_fragment.*
import javax.inject.Inject

class ComingTVShowsFragment : Fragment(), OnShowMovieSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactoryDI
    private lateinit var viewModel: ComingTvshowsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.coming_tvshows_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ((activity!! as MainActivity).application as MovieApp)
            .appComponent
            .getFragmentComponent()
            .create(activity!!)
            .inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComingTvshowsViewModel::class.java)

        initList()
    }

    private fun initList(){
        val mAdapter = ComingTVShowsAdapter()
        mAdapter.setListener(this)
        viewModel.refreshTv()
        coming_tv_shows_list.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
        }
        viewModel.resultTv.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        viewModel.networkStateTv.observe(viewLifecycleOwner, Observer {
            mAdapter.setNetworkState(it)
        })
        viewModel.getGenres().observe(viewLifecycleOwner, Observer {
            mAdapter.setGenres(it)
        })

    }

    override fun onItemSelected(view: View, id: Int, contentType: Int) {
        when(contentType){
            OnShowMovieSelectedListener.TV_SHOW_TYPE -> {
                val option = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,
                    Pair(view.findViewById<ImageView>(R.id.coming_poster), "headPoster$id")
                )
                val intent = Intent(activity!!, TvDetailsActivity::class.java)
                intent.putExtra(OnShowMovieSelectedListener.CONTENT_ID, id)
                startActivity(intent, option.toBundle())
            }
        }
    }
}
