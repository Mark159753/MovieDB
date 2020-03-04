package com.example.moviedb.ui.person

import android.app.ActivityOptions
import android.content.Context
import android.content.Intent
import android.graphics.Outline
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.Pair
import android.view.Menu
import android.view.View
import android.view.ViewOutlineProvider
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.MovieApp
import com.example.moviedb.R
import com.example.moviedb.databinding.ActivityPersonBinding
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.ui.base.OnShowMovieSelectedListener
import com.example.moviedb.ui.detaile.MovieDetailActivity
import com.example.moviedb.ui.home.adapter.MarginDecorator
import com.example.moviedb.ui.person.adapter.KnownForRCAdapter
import com.example.moviedb.ui.tvDetails.TvDetailsActivity
import com.example.moviedb.until.LocaleHelper
import kotlinx.android.synthetic.main.activity_person.*
import javax.inject.Inject

class PersonActivity : AppCompatActivity(), OnShowMovieSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelFactoryDI
    private lateinit var viewModel: PersonViewModel
    private lateinit var binding:ActivityPersonBinding

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!, LocaleHelper.getLanguage(newBase)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_person)
        binding.lifecycleOwner = this
        (application as MovieApp).appComponent.getFragmentComponent().create(this).inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(PersonViewModel::class.java)

        setSupportActionBar(binding.personToolbar)

        val id = intent.getIntExtra(OnShowMovieSelectedListener.CONTENT_ID, 0)

        ViewCompat.setTransitionName(binding.personPhoto, "headPoster${id}")
        ViewCompat.setTransitionName(binding.personName, "headTitle${id}")

        initMenu()
        loadDetails(id)
        initKnowForList(id)
    }

    private fun loadDetails(id:Int){
        binding.person = viewModel.getPersonDetail(id, "en-US")

        binding.personScrollContent.post {
            binding.personScrollContent.scrollTo(0,0)
        }
    }

    private fun initKnowForList(id: Int){
        val mAdapter = KnownForRCAdapter()
        mAdapter.setListener(this)
        binding.personKnownForList.apply {
            adapter = mAdapter
            setHasFixedSize(true)
            addItemDecoration(MarginDecorator(15, 15))
        }
        viewModel.getPersonKnowForCast(id, LocaleHelper.getLanguage(this)).observe(this, Observer {
            if (it == null) return@Observer
            mAdapter.setDataList(it)
        })
    }

    private fun initMenu(){
        supportActionBar?.title = ""
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.person_close, menu)
        menu?.findItem(R.id.person_close_btn)?.setOnMenuItemClickListener {
            onBackPressed()
            true
        }
        return  true
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
            OnShowMovieSelectedListener.PERSON_TYPE -> {}
        }
    }
}
