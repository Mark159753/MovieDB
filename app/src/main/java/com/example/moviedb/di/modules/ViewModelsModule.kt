package com.example.moviedb.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.di.ViewModelKey
import com.example.moviedb.ui.home.HomeViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelsModule {

    @Binds
    @IntoMap
    @ViewModelKey(HomeViewModel::class)
    abstract fun bindHomeViewModel(model:HomeViewModel):ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory:ViewModelFactoryDI):ViewModelProvider.Factory
}