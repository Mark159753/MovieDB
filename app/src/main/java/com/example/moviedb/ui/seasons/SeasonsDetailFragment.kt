package com.example.moviedb.ui.seasons


import android.os.Bundle
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
import com.example.moviedb.ui.seasons.adapter.EpisodeListAdapterRC
import com.example.moviedb.until.LocaleHelper
import kotlinx.android.synthetic.main.fragment_seasons_detail.*
import javax.inject.Inject

private const val TV_SHOW_ID = "com.example.moviedb.ui.seasons.param1"
private const val SEASON_NUMBER = "com.example.moviedb.ui.seasons.param2"


class SeasonsDetailFragment : Fragment() {
    private var tvShowId: Int? = null
    private var seasonNumber: Int? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactoryDI
    private lateinit var viewModel: SeasonViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            tvShowId = it.getInt(TV_SHOW_ID)
            seasonNumber = it.getInt(SEASON_NUMBER)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_seasons_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity!!.application as MovieApp).appComponent.getFragmentComponent().create(activity!!).inject(this)

        viewModel = ViewModelProvider(viewModelStore, viewModelFactory).get(SeasonViewModel::class.java)
        initEpisodeList()
    }

    private fun initEpisodeList(){
        val mAdapter = EpisodeListAdapterRC()
        season_list_of_episode.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(DividerItemDecoration(activity!!, DividerItemDecoration.VERTICAL))
        }
        if (tvShowId != null && seasonNumber != null) {
            val response = viewModel.getEpisodeList(
                tvShowId!!,
                seasonNumber!!,
                LocaleHelper.getLanguage(activity!!)
            )
            response.observe(activity!!, Observer {
                if (it == null) return@Observer
                season_progress_bar.visibility = View.GONE
                mAdapter.setLisOfData(it.episodes)
            })
        }
    }

    companion object {
        fun newInstance(tvShowId: Int, SeasonNumber: Int) =
            SeasonsDetailFragment().apply {
                arguments = Bundle().apply {
                    putInt(TV_SHOW_ID, tvShowId)
                    putInt(SEASON_NUMBER, SeasonNumber)
                }
            }
    }
}
