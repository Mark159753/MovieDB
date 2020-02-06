package com.example.moviedb.ui.coming.fragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.moviedb.MovieApp

import com.example.moviedb.R
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.ui.MainActivity
import com.example.moviedb.ui.coming.adapter.ComingMoviesAdapter
import kotlinx.android.synthetic.main.coming_movie_fragment.*
import javax.inject.Inject

class ComingMovieFragment : Fragment() {

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
        viewModel.refreshMovies()
        coming_movie_list.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
        }
        viewModel.resultMovies.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        viewModel.networkStateMovie.observe(viewLifecycleOwner, Observer {
            mAdapter.setNetworkState(it)
        })
        viewModel.getGenres().observe(viewLifecycleOwner, Observer {
            mAdapter.setGenres(it)
        })
    }

}
