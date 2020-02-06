package com.example.moviedb.ui.coming.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.moviedb.R
import com.example.moviedb.ui.coming.fragment.ComingMovieFragment
import com.example.moviedb.ui.coming.fragment.ComingTVShowsFragment
import java.lang.IllegalStateException

class ComingViewPagerAdapter(supportFragmentManager: FragmentManager,
                             private val context: Context): FragmentStatePagerAdapter(supportFragmentManager, FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> ComingMovieFragment()
            1 -> ComingTVShowsFragment()
            else -> throw IllegalStateException("UNKNOWN POSITION")
        }
    }

    override fun getCount(): Int {
        return 2
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return when(position){
            0 -> context.resources.getString(R.string.coming_pager_movie)
            1 -> context.resources.getString(R.string.coming_pager_shows)
            else -> null
        }
    }
}