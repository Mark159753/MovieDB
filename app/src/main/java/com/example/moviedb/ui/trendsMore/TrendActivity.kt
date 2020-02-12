package com.example.moviedb.ui.trendsMore

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.PopupMenu
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.MovieApp
import com.example.moviedb.R
import com.example.moviedb.data.repository.trends.TrendsRepository
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.ui.trendsMore.adapter.TrendRCDecorator
import com.example.moviedb.ui.trendsMore.adapter.TrendsOfDayRCAdapter
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_trend.*
import javax.inject.Inject

class TrendActivity : AppCompatActivity() {

    private val SAVE_MEDIA_TYPE = "SAVE_MEDIA_TYPE"

    @Inject
    lateinit var viewModelFactory: ViewModelFactoryDI
    private lateinit var viewModel:TrendsViewModel
    private lateinit var toolBar:Toolbar
    private lateinit var todayListAdapter:TrendsOfDayRCAdapter
    private lateinit var weekListAdapter:TrendsOfDayRCAdapter

    private var mediaType = TrendsRepository.TREND_ALL_TYPE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trend)
        (application as MovieApp).appComponent.getFragmentComponent().create(this).inject(this)

        restoreMediaType(savedInstanceState)

        viewModel = ViewModelProvider(this, viewModelFactory).get(TrendsViewModel::class.java)
        toolBar = findViewById(R.id.trend_tool_bar)
        toolBar.title = "Trending"
        setSupportActionBar(toolBar)

        initToolBar()
        initTabs()
        initTrendMenu()
        initTodayList()
        initWeekList()

    }

    private fun initTrendMenu(){
        val popup = PopupMenu(this, trends_menu_btn)
        popup.menuInflater.inflate(R.menu.media_type_menu, popup.menu)
        restoreMenuCheck(popup.menu)
        popup.setOnMenuItemClickListener {
            when(it.itemId){
                R.id.trend_all_menu -> {
                    if (it.isChecked()) it.setChecked(false)
                    else it.setChecked(true)
                    this.mediaType = TrendsRepository.TREND_ALL_TYPE
                    updateTrendListData()
                    updateTrendWeekListData()
                    return@setOnMenuItemClickListener true
                }
                R.id.trend_movies_menu -> {
                    if (it.isChecked()) it.setChecked(false)
                    else it.setChecked(true)
                    this.mediaType = TrendsRepository.TREND_MOVIE_TYPE
                    updateTrendListData()
                    updateTrendWeekListData()
                    return@setOnMenuItemClickListener true
                }
                R.id.trend_tv_shows_menu -> {
                    if (it.isChecked()) it.setChecked(false)
                    else it.setChecked(true)
                    this.mediaType = TrendsRepository.TREND_TV_SHOW_TYPE
                    updateTrendListData()
                    updateTrendWeekListData()
                    return@setOnMenuItemClickListener true
                }
                R.id.trend_person_menu -> {
                    if (it.isChecked()) it.setChecked(false)
                    else it.setChecked(true)
                    this.mediaType = TrendsRepository.TREND_PERSON_TYPE
                    updateTrendListData()
                    updateTrendWeekListData()
                    return@setOnMenuItemClickListener true
                }
            }
            return@setOnMenuItemClickListener super.onOptionsItemSelected(it)
        }
        trends_menu_btn.setOnClickListener {
            popup.show()
        }
    }


    private fun restoreMenuCheck(menu:Menu){
        when(mediaType){
            TrendsRepository.TREND_ALL_TYPE -> {menu.findItem(R.id.trend_all_menu).isChecked = true}
            TrendsRepository.TREND_MOVIE_TYPE -> {menu.findItem(R.id.trend_movies_menu).isChecked = true}
            TrendsRepository.TREND_TV_SHOW_TYPE -> {menu.findItem(R.id.trend_tv_shows_menu).isChecked = true}
            TrendsRepository.TREND_PERSON_TYPE -> {menu.findItem(R.id.trend_person_menu).isChecked = true}
        }
    }


    private fun initTodayList(){
        todayListAdapter = TrendsOfDayRCAdapter(this)
        trend_today_list.apply {
            adapter = todayListAdapter
            setHasFixedSize(true)
            addItemDecoration(TrendRCDecorator(30, 15))
        }
        updateTrendListData()
    }

    private fun initWeekList(){
        weekListAdapter = TrendsOfDayRCAdapter(this)
        trend_week_list.apply {
            adapter = weekListAdapter
            setHasFixedSize(true)
            addItemDecoration(TrendRCDecorator(30, 15))
        }
        updateTrendWeekListData()
    }

    private fun updateTrendWeekListData(){
        val data = viewModel.getTrendsOfType(this.mediaType, "uk-uk", TrendsRepository.WEEK_TIME_WINDOW)
        data.pagedList.observe(this, Observer {
            weekListAdapter.submitList(it)
        })
        data.networkState.observe(this, Observer {
            weekListAdapter.setNetworkState(it)
        })
        viewModel.getGenres().observe(this, Observer {
            weekListAdapter.setGenres(it)
        })
    }

    private fun updateTrendListData(){
        val data = viewModel.getTrendsOfType(this.mediaType, "uk=uk", TrendsRepository.DAY_TIME_WINDOW)
       data.pagedList.observe(this, Observer {
            todayListAdapter.submitList(it)
        })
        data.networkState.observe(this, Observer {
            todayListAdapter.setNetworkState(it)
        })
        viewModel.getGenres().observe(this, Observer {
            todayListAdapter.setGenres(it)
        })
    }

    private fun initToolBar(){
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        toolBar.navigationIcon = resources.getDrawable(R.drawable.ic_arrow_back_black_24dp)
        toolBar.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun initTabs(){
        trend_head_tabs.addOnTabSelectedListener(object :TabLayout.OnTabSelectedListener{
            override fun onTabReselected(p0: TabLayout.Tab?) {}
            override fun onTabUnselected(p0: TabLayout.Tab?) {}

            override fun onTabSelected(tab: TabLayout.Tab?) {
                val position = tab!!.position
                when(position){
                    0 -> {
                        trend_week_list.visibility = View.GONE
                        trend_today_list.visibility = View.VISIBLE
                    }
                    1 -> {
                        trend_week_list.visibility = View.VISIBLE
                        trend_today_list.visibility = View.GONE
                    }
                }
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SAVE_MEDIA_TYPE, mediaType)
    }

    private fun restoreMediaType(savedInstanceState: Bundle?){
        val type = savedInstanceState?.getString(SAVE_MEDIA_TYPE, TrendsRepository.TREND_ALL_TYPE)
        type?.let {
            mediaType = it
        }
    }
}
