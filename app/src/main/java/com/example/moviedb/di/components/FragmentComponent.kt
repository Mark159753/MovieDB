package com.example.moviedb.di.components

import androidx.fragment.app.FragmentActivity
import com.example.moviedb.di.modules.SharedPrefferencesModule
import com.example.moviedb.di.scope.FragmentScope
import com.example.moviedb.ui.coming.fragment.ComingMovieFragment
import com.example.moviedb.ui.coming.fragment.ComingTVShowsFragment
import com.example.moviedb.ui.discover.DiscoverFragment
import com.example.moviedb.ui.home.HomeFragment
import com.example.moviedb.ui.trendsMore.TrendActivity
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [SharedPrefferencesModule::class])
interface FragmentComponent {

    fun inject(main:HomeFragment)

    fun inject(main:DiscoverFragment)

    fun inject(main:ComingMovieFragment)

    fun inject(main:ComingTVShowsFragment)

    fun inject(main:TrendActivity)

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: FragmentActivity):FragmentComponent
    }
}