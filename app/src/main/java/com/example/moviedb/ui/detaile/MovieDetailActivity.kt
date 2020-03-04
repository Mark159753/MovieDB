package com.example.moviedb.ui.detaile

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.transition.Transition
import android.util.Log
import android.util.Pair
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.navArgs
import com.example.moviedb.MovieApp
import com.example.moviedb.R
import com.example.moviedb.databinding.ActivityMovieDetailBinding
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.example.moviedb.ui.detaile.adapter.CastRcAdapter
import com.example.moviedb.ui.detaile.adapter.SimilarRCAdapter
import com.example.moviedb.ui.home.adapter.MarginDecorator
import com.example.moviedb.ui.person.PersonActivity
import com.example.moviedb.ui.tvDetails.TvDetailsActivity
import com.example.moviedb.ui.youTube.YoutubeActivity
import com.example.moviedb.until.LocaleHelper
import com.example.moviedb.until.NetworkState

import kotlinx.android.synthetic.main.activity_movie_detail.*
import javax.inject.Inject

class MovieDetailActivity : AppCompatActivity(), OnShowMovieSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactoryDI
    private lateinit var viewModel: MovieDetailViewModel
    private lateinit var binding: ActivityMovieDetailBinding

    private var refreshPoster = false

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!, LocaleHelper.getLanguage(newBase)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_movie_detail)
        binding.lifecycleOwner = this
        (application as MovieApp).appComponent.getFragmentComponent().create(this).inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(MovieDetailViewModel::class.java)

        setSupportActionBar(binding.movieDetailToolbar)

        val id = intent.getIntExtra(OnShowMovieSelectedListener.CONTENT_ID, 0)
        ViewCompat.setTransitionName(binding.detailMoviePoster, "headPoster$id")
        ViewCompat.setTransitionName(binding.detailTitle, "headTitle$id")

        transitionHelper()
        initLoadData(id)
        initCastList(id)
        initSimilarMovieList(id)
        initMenu()
        initToolbarAnim()
        initLoadVideos(id)

    }

    private fun initLoadVideos(id: Int){
        viewModel.getMovieVideos(id, LocaleHelper.getLanguage(this)).observe(this, Observer {
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
        detail_fab.setOnClickListener { view ->
            if (videoMissing){
                Snackbar.make(binding.root, R.string.missing_video, Snackbar.LENGTH_SHORT).show()
            }else {
                val intent = Intent(this, YoutubeActivity::class.java)
                intent.putExtra(YoutubeActivity.VIDEO_ID, id)
                startActivity(intent)
            }
        }
    }


    private fun transitionHelper(){
        window.sharedElementEnterTransition.addListener(object:Transition.TransitionListener{
            override fun onTransitionEnd(transition: Transition?) {
                refreshPoster = !refreshPoster
                if (refreshPoster)
                    binding.detailMoviePoster.requestLayout()
            }
            override fun onTransitionResume(transition: Transition?) {}
            override fun onTransitionPause(transition: Transition?) {}
            override fun onTransitionCancel(transition: Transition?) {}
            override fun onTransitionStart(transition: Transition?) {}
        })
    }

    private fun initCastList(id: Int){
        val mAdapter = CastRcAdapter()
        mAdapter.setListener(this)
        binding.detailTopCastList.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(MarginDecorator(15, 15))
        }
        val response = viewModel.getMovieCast(id, LocaleHelper.getLanguage(this))
        response.response?.observe(this, Observer {
            if (it == null) return@Observer
            mAdapter.setList(it.cast)
        })
        response.networkState.observe(this, Observer {
            mAdapter.setNetworkState(it)
        })
    }

    private fun initSimilarMovieList(id: Int){
        val mAdapter = SimilarRCAdapter()
        mAdapter.setListener(this)
        binding.detailSimilarMovieList.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(MarginDecorator(15, 15))
        }
        val response = viewModel.getSimilarMovies(id, LocaleHelper.getLanguage(this))
        response.pagedList.observe(this, Observer {
            mAdapter.submitList(it)
            if (it.size == 0) {
                binding.detailSimilarMovieList.visibility = View.GONE
                binding.detailSimilarMovieTitle.visibility = View.GONE
            }
        })
        response.networkState.observe(this, Observer {
            mAdapter.setNetworkState(it)
        })
    }

    private fun initLoadData(id: Int) {
        val response = viewModel.getMovieDetails(id, LocaleHelper.getLanguage(this))
        response.networkState.observe(this, Observer {
            if (it is NetworkState.ERROR) {
                binding.motionLayout.visibility = View.GONE
                binding.detailErrorLayout.visibility = View.VISIBLE
                binding.detailErrorMsg.text = it.msg
            }
        })
        binding.movie = response.response
        binding.detailContent.post {
            binding.detailContent.scrollTo(0,0)
        }
    }

    private fun initToolbarAnim() {
        binding.motionLayout.setTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {}
            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {}

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                binding.detailToolbarMotion.progress = p3
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                p0?.let { binding.detailToolbarMotion.progress = it.progress }
            }
        })
    }

    private fun initMenu() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        supportActionBar?.title = ""
        binding.movieDetailToolbar.setNavigationOnClickListener {
            onBackPressed()
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
