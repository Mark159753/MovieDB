package com.example.moviedb.ui.seasons

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.moviedb.R
import com.example.moviedb.model.tvDetails.Season
import com.example.moviedb.ui.seasons.adapter.SeasonVPAdapter
import com.example.moviedb.until.LocaleHelper
import kotlinx.android.synthetic.main.activity_seasons.*

class SeasonsActivity : AppCompatActivity() {

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase!!, LocaleHelper.getLanguage(newBase)))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_seasons)

        val seasonList = intent.getParcelableArrayListExtra<Season>(SEASON_LIST)?.reversed()
        val toolBarTitle = intent.getStringExtra(TITLE)
        val tvShowId = intent.getIntExtra(TV_SHOW_ID, 0)

        setSupportActionBar(season_toolbar)
        supportActionBar?.title = toolBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        season_toolbar.setNavigationOnClickListener { onBackPressed() }

        val mAdapter = SeasonVPAdapter(supportFragmentManager, seasonList!!, tvShowId)
        seasons_viewPager.adapter = mAdapter
        season_tabs.setupWithViewPager(seasons_viewPager)
    }



    companion object{
        const val SEASON_LIST = "com.example.moviedb.ui.seasons.SEASON_LIST"
        const val TITLE = "com.example.moviedb.ui.seasons.TITLE"
        const val TV_SHOW_ID = "com.example.moviedb.ui.seasons.TV_SHOW_ID"
    }
}
