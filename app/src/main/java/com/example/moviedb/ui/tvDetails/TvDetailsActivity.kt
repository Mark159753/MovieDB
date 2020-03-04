package com.example.moviedb.ui.tvDetails

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.MovieApp
import com.example.moviedb.R
import com.example.moviedb.databinding.ActivityTvDetailsBinding
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.model.tvDetails.Season
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.example.moviedb.ui.detaile.MovieDetailActivity
import com.example.moviedb.ui.detaile.adapter.CastRcAdapter
import com.example.moviedb.ui.home.adapter.MarginDecorator
import com.example.moviedb.ui.person.PersonActivity
import com.example.moviedb.ui.seasons.SeasonsActivity
import com.example.moviedb.ui.youTube.YoutubeActivity
import com.example.moviedb.until.LocaleHelper
import com.google.android.material.snackbar.Snackbar
import javax.inject.Inject

class TvDetailsActivity : AppCompatActivity(), OnShowMovieSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactoryDI
    private lateinit var viewModel: TvDetailsViewModel
    private lateinit var binding:ActivityTvDetailsBinding
    private var seasonsList:ArrayList<Season>? = null
    private var tvTitle:String? = null
    private var tvId:Int = 0

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!, LocaleHelper.getLanguage(newBase)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_tv_details)
        binding.lifecycleOwner = this

        (application as MovieApp).appComponent.getFragmentComponent().create(this).inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(TvDetailsViewModel::class.java)

        val id = intent.getIntExtra(OnShowMovieSelectedListener.CONTENT_ID, 0)

        ViewCompat.setTransitionName(binding.tvDetailsPoster, "headPoster$id")

        initToolBar()
        loadDate(id)
        initCastList(id)
        initLoadVideos(id)
    }

    private fun initToolBar(){
        setSupportActionBar(binding.tvDetailsToolBar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        binding.tvDetailsToolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun loadDate(id:Int){
        val response = viewModel.getTvDetails(id, LocaleHelper.getLanguage(this))
        binding.details = response
        response.observe(this, Observer {
            if (it == null) return@Observer
            seasonsList = ArrayList(it.seasons)
            tvTitle = it.name
            tvId = it.id
        })

        binding.tvDetailsSesonsBtn.setOnClickListener {
           val intent = Intent(this, SeasonsActivity::class.java)
            intent.putExtra(SeasonsActivity.SEASON_LIST, seasonsList)
            intent.putExtra(SeasonsActivity.TITLE, tvTitle)
            intent.putExtra(SeasonsActivity.TV_SHOW_ID, tvId)
            seasonsList?.let {
                startActivity(intent)
            }
        }

        binding.tvDetailsScrolling.post {
            binding.tvDetailsScrolling.scrollTo(0,0)
        }
    }

    private fun initCastList(id: Int){
        val mAdapter = CastRcAdapter()
        mAdapter.setListener(this)
        binding.tvDetailsTopCastList.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(MarginDecorator(15, 15))
        }
        val response = viewModel.getTvCasts(id, LocaleHelper.getLanguage(this))
        response.observe(this, Observer {
            if (it == null) return@Observer
            mAdapter.setList(it)
        })
    }

    private fun initLoadVideos(id: Int){
        viewModel.getTvVideos(id, LocaleHelper.getLanguage(this)).observe(this, Observer {
            if (it == null) return@Observer
            if (it.isEmpty()){
                initVideoPlayBTN("", true)
                return@Observer
            }
            for (i in it){
                if (i.site.equals("YouTube", true)){
                    initVideoPlayBTN(i.key, false)
                    break
                }
            }
        })
    }

    private fun initVideoPlayBTN(id: String, videoMissing:Boolean){
        binding.tvDetailsFab.setOnClickListener { view ->
            if (videoMissing){
                Snackbar.make(binding.root, R.string.missing_video, Snackbar.LENGTH_SHORT).show()
            }else {
                val intent = Intent(this, YoutubeActivity::class.java)
                intent.putExtra(YoutubeActivity.VIDEO_ID, id)
                startActivity(intent)
            }
        }
    }

    override fun onItemSelected(view: View, id: Int, contentType: Int) {
        when(contentType){
            OnShowMovieSelectedListener.MOVIE_TYPE -> {
                val option = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair(view.findViewById<ImageView>(R.id.head_poster_img), "headPoster$id"),
                    Pair(view.findViewById<TextView>(R.id.movie_title), "headTitle$id"),
                    Pair(view.findViewById<TextView>(R.id.movie_title), "headTitle$id")
                )
                val intent = Intent(this, MovieDetailActivity::class.java)
                intent.putExtra(OnShowMovieSelectedListener.CONTENT_ID, id)
                startActivity(intent, option.toBundle())
            }
            OnShowMovieSelectedListener.TV_SHOW_TYPE -> {
                val option = ActivityOptionsCompat.makeSceneTransitionAnimation(this,
                    androidx.core.util.Pair(
                        view.findViewById<ImageView>(R.id.head_poster_img),
                        "headPoster$id"
                    )
                )
                val intent = Intent(this, TvDetailsActivity::class.java)
                intent.putExtra(OnShowMovieSelectedListener.CONTENT_ID, id)
                startActivity(intent, option.toBundle())
            }
            OnShowMovieSelectedListener.PERSON_TYPE -> {
                val intent = Intent(this, PersonActivity::class.java)
                intent.putExtra(OnShowMovieSelectedListener.CONTENT_ID, id)
                val options = ActivityOptions.makeSceneTransitionAnimation(this,
                    Pair.create(view.findViewById<ImageView>(R.id.head_poster_img), "headPoster${id}"),
                    Pair.create(view.findViewById<TextView>(R.id.movie_title), "headTitle${id}"),
                    Pair.create(view.findViewById<TextView>(R.id.movie_title), "headTitle${id}")
                )
                startActivity(intent, options.toBundle())
            }
        }
    }
}
