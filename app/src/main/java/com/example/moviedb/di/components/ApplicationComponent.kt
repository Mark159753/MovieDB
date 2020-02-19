package com.example.moviedb.di.components

import android.content.Context
import com.example.moviedb.di.modules.ApplicationModule
import com.example.moviedb.di.modules.ViewModelsModule
import com.example.moviedb.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [
    ViewModelsModule::class,
    ApplicationModule::class
])
interface ApplicationComponent {

    fun getFragmentComponent():FragmentComponent.Factory

    fun inject(main:MainActivity)

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance applicationContext: Context):ApplicationComponent
    }

}