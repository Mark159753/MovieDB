package com.example.moviedb.ui.home

import android.content.Intent
import androidx.lifecycle.ViewModelProviders
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
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.ActivityNavigator
import androidx.navigation.findNavController
import com.example.moviedb.MovieApp

import com.example.moviedb.R
import com.example.moviedb.databinding.HomeFragmentBinding
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.ui.MainActivity
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.example.moviedb.ui.detaile.MovieDetailActivity
import com.example.moviedb.ui.home.adapter.MarginDecorator
import com.example.moviedb.ui.home.adapter.MovieRvAdapter
import com.example.moviedb.ui.home.adapter.PopularMoviesPagerAdapter
import com.example.moviedb.ui.home.adapter.TvRvAdapter
import com.example.moviedb.ui.tvDetails.TvDetailsActivity
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.home_fragment.*
import javax.inject.Inject

class HomeFragment : Fragment(), OnShowMovieSelectedListener {

    private lateinit var binding:HomeFragmentBinding

    @Inject
    lateinit var viewModelFactory:ViewModelFactoryDI
    private lateinit var viewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate<HomeFragmentBinding>(inflater,
            R.layout.home_fragment, container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        ((activity!! as MainActivity).application as MovieApp)
            .appComponent
            .getFragmentComponent()
            .create(activity!!)
            .inject(this)

        viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)
        binding.model = viewModel

        initTabs()
        initInTheatreAdapter()
        initOnTvAdapter()
        initPopularMovies()
        initOpenMoreTrendsBtn()
    }


    override fun onResume() {
        super.onResume()
        binding.popularSlider.resumeAutoScroll()
    }

    override fun onPause() {
        binding.popularSlider.pauseAutoScroll()
        super.onPause()
    }

    private fun initTabs(){
        binding.headTabs.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                when(position){
                    0 -> {
                        binding.onTvList.visibility = View.GONE
                        binding.inTheaterList.visibility = View.VISIBLE
                    }
                    1 -> {
                        binding.inTheaterList.visibility = View.GONE
                        binding.onTvList.visibility = View.VISIBLE
                    }
                }
            }
        })
    }

    private fun initInTheatreAdapter(){
        val mAdapter =  MovieRvAdapter()
        mAdapter.setListener(this)
        viewModel.refreshMovies()
        in_theater_list.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(MarginDecorator(15,15))
        }
        viewModel.resultMovies.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })
        viewModel.networkStateMovie.observe(viewLifecycleOwner, Observer {
            mAdapter.setNetworkState(it)
        })
    }

    private fun initOnTvAdapter(){
        val mAdapter = TvRvAdapter()
        mAdapter.setListener(this)
        viewModel.refreshTv()
        binding.onTvList.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(MarginDecorator(15, 15))
        }
        viewModel.resultTv.observe(viewLifecycleOwner, Observer {
            mAdapter.submitList(it)
        })

        viewModel.networkStateTv.observe(viewLifecycleOwner, Observer {
            mAdapter.setNetworkState(it)
        })
    }

    private fun initPopularMovies(){
        viewModel.popularMovies.observe(viewLifecycleOwner, Observer {
            if (it.isEmpty()) viewModel.loadPopularMovies()
            binding.popularProgressBar.visibility = View.GONE
            binding.popularSlider.apply {
                visibility = View.VISIBLE
                adapter = PopularMoviesPagerAdapter(this@HomeFragment.activity!!, it, true)
                pageMargin = 24
            }
        })
    }

    override fun onItemSelected(view: View, id: Int, contentType: Int) {
        Log.e("ID", id.toString())
        when(contentType){
            OnShowMovieSelectedListener.MOVIE_TYPE -> Log.e("TYPE", "MOVIE")
            OnShowMovieSelectedListener.TV_SHOW_TYPE -> Log.e("TYPE", "TV_SHOW_SHOW")
            OnShowMovieSelectedListener.PERSON_TYPE -> Log.e("TYPE", "PERSON")
        }

        when(contentType){
            OnShowMovieSelectedListener.MOVIE_TYPE -> {
                val option = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,
                    Pair(view.findViewById<ImageView>(R.id.head_poster_img), "headPoster$id"),
                    Pair(view.findViewById<TextView>(R.id.movie_title), "headTitle$id"),
                    Pair(view.findViewById<TextView>(R.id.movie_title), "headTitle$id")
                )
                val intent = Intent(activity!!, MovieDetailActivity::class.java)
                intent.putExtra(OnShowMovieSelectedListener.CONTENT_ID, id)
                startActivity(intent, option.toBundle())
            }
            OnShowMovieSelectedListener.TV_SHOW_TYPE -> {
                val option = ActivityOptionsCompat.makeSceneTransitionAnimation(activity!!,
                    Pair(view.findViewById<ImageView>(R.id.head_poster_img), "headPoster$id")
                )
                val intent = Intent(activity!!, TvDetailsActivity::class.java)
                intent.putExtra(OnShowMovieSelectedListener.CONTENT_ID, id)
                startActivity(intent, option.toBundle())
            }
            OnShowMovieSelectedListener.PERSON_TYPE -> {}
        }
    }

    private fun initOpenMoreTrendsBtn(){
        binding.homeMoreBtn.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_trendActivity)
        }
    }

}
