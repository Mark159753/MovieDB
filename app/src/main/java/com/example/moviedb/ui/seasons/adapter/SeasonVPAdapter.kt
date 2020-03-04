package com.example.moviedb.ui.seasons.adapter

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.example.moviedb.model.tvDetails.Season
import com.example.moviedb.ui.seasons.SeasonsDetailFragment

class SeasonVPAdapter(
    supportFragmentManager: FragmentManager,
    private val listOfData:List<Season>,
    private val tvShowId:Int
) : FragmentStatePagerAdapter(supportFragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment {
        return SeasonsDetailFragment.newInstance(tvShowId, listOfData[position].seasonNumber)
    }

    override fun getCount(): Int {
        return listOfData.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listOfData[position].name
    }
}