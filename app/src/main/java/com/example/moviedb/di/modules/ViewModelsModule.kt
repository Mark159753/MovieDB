package com.example.moviedb.di.modules

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.moviedb.di.ViewModelFactoryDI
import com.example.moviedb.di.ViewModelKey
import com.example.moviedb.ui.coming.fragment.ComingMovieViewModel
import com.example.moviedb.ui.coming.fragment.ComingTvshowsViewModel
import com.example.moviedb.ui.detaile.MovieDetailViewModel
import com.example.moviedb.ui.discover.DiscoverViewModel
import com.example.moviedb.ui.home.HomeViewModel
import com.example.moviedb.ui.person.PersonViewModel
import com.example.moviedb.ui.seasons.SeasonViewModel
import com.example.moviedb.ui.trendsMore.TrendsViewModel
import com.example.moviedb.ui.tvDetails.TvDetailsViewModel
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
    @IntoMap
    @ViewModelKey(DiscoverViewModel::class)
    abstract fun bindDiscoverViewModel(model:DiscoverViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComingMovieViewModel::class)
    abstract fun bindComingMovieViewModel(model:ComingMovieViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ComingTvshowsViewModel::class)
    abstract fun bindComingTvshowsViewModel(model:ComingTvshowsViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TrendsViewModel::class)
    abstract fun bindTrendsViewModel(model:TrendsViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MovieDetailViewModel::class)
    abstract fun bindMovieDetailViewModel(model:MovieDetailViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PersonViewModel::class)
    abstract fun bindPersonViewModel(model:PersonViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TvDetailsViewModel::class)
    abstract fun bindTvDetailsViewModel(model:TvDetailsViewModel):ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SeasonViewModel::class)
    abstract fun bindSeasonViewModel(model:SeasonViewModel):ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory:ViewModelFactoryDI):ViewModelProvider.Factory
}