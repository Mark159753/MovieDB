package com.example.moviedb.ui.coming.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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
import com.example.moviedb.ui.coming.adapter.ComingMoviesAdapter
import com.example.moviedb.ui.detaile.MovieDetailActivity
import kotlinx.android.synthetic.main.coming_movie_fragment.*
import javax.inject.Inject

class ComingMovieFragment : Fragment(), OnShowMovieSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactoryDI
    private lateinit var viewModel: ComingMovieViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.coming_movie_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ((activity!! as MainActivity).application as MovieApp)
            .appComponent
            .getFragmentComponent()
            .create(activity!!)
            .inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(ComingMovieViewModel::class.java)

        initList()
    }


    private fun initList(){
        val mAdapter = ComingMoviesAdapter()
        mAdapter.setListener(this)
        mAdapter.currentList?.dataSource?.invalidate()
        viewModel.refreshMovies()
        coming_movie_list.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
        }
        viewModel.resultMovies.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        // Found some Problem need to fixed
//        viewModel.networkStateMovie.observe(activity!!, Observer {
//            mAdapter.setNetworkState(it)
//        })
        viewModel.getGenres().observe(viewLifecycleOwner, Observer {
            mAdapter.setGenres(it)
        })
    }

    override fun onItemSelected(view: View, id: Int, contentType: Int) {
        when(contentType){
            OnShowMovieSelectedListener.MOVIE_TYPE -> {
                val option = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,
                Pair(view.findViewById<ImageView>(R.id.coming_poster), "headPoster$id"),
                Pair(view.findViewById<TextView>(R.id.coming_title), "headTitle$id"),
                Pair(view.findViewById<TextView>(R.id.coming_title), "headTitle$id")
            )
                val intent = Intent(activity!!, MovieDetailActivity::class.java)
                intent.putExtra(OnShowMovieSelectedListener.CONTENT_ID, id)
                startActivity(intent, option.toBundle())
            }
        }
    }
}
