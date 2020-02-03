package com.example.moviedb.di.components

import androidx.fragment.app.FragmentActivity
import com.example.moviedb.di.modules.SharedPrefferencesModule
import com.example.moviedb.di.scope.FragmentScope
import com.example.moviedb.ui.discover.DiscoverFragment
import com.example.moviedb.ui.home.HomeFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [SharedPrefferencesModule::class])
interface FragmentComponent {

    fun inject(main:HomeFragment)

    fun inject(main:DiscoverFragment)

    @Subcomponent.Factory
    interface Factory{
        fun create(@BindsInstance activity: FragmentActivity):FragmentComponent
    }
}