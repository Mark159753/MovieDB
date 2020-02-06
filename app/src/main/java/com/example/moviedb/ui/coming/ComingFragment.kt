package com.example.moviedb.ui.coming

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.ViewPager

import com.example.moviedb.R
import com.example.moviedb.ui.coming.adapter.ComingViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import okhttp3.internal.notifyAll

class ComingFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.coming_fragment, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val pager = view.findViewById<ViewPager>(R.id.coming_viewpager)
        val tabs = view.findViewById<TabLayout>(R.id.coming_tabs)
        val mAdapter = ComingViewPagerAdapter(activity!!.supportFragmentManager, activity!!)
        pager.adapter = mAdapter
        tabs.setupWithViewPager(pager)
    }

}
