package com.example.moviedb.di.components

import android.content.Context
import com.example.moviedb.di.modules.ApplicationModule
import com.example.moviedb.di.modules.ViewModelsModule
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

    @Component.Factory
    interface Factory{
        fun create(@BindsInstance applicationContext: Context):ApplicationComponent
    }

}