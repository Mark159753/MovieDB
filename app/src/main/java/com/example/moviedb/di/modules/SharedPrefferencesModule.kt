package com.example.moviedb.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import com.example.moviedb.data.local.dao.*
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.data.repository.Repository
import com.example.moviedb.data.repository.RepositoryImpl
import com.example.moviedb.data.repository.discover.DiscoverRepository
import com.example.moviedb.data.repository.discover.DiscoverRepositoryImpl
import com.example.moviedb.data.repository.trends.TrendsRepository
import com.example.moviedb.data.repository.trends.TrendsRepositoryImpl
import com.example.moviedb.di.scope.FragmentScope
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor

@Module
object SharedPrefferencesModule {

    @JvmStatic
    @FragmentScope
    @Provides
    fun providePreferences(activity: FragmentActivity): SharedPreferences {
        return activity.getPreferences(Context.MODE_PRIVATE)
    }

    @JvmStatic
    @FragmentScope
    @Provides
    fun provideRepository(popularMoviesDao: PopularMoviesDao,
                          server: TMDBserver, ioExecutor: Executor, preferences: SharedPreferences): Repository {
        return RepositoryImpl(popularMoviesDao, server, ioExecutor, preferences)
    }

    @JvmStatic
    @FragmentScope
    @Provides
    fun provideDiscoverRepository(context: Context, apiServer:TMDBserver, ioExecutor: Executor, genreDao: GenreDao):DiscoverRepository{
        return DiscoverRepositoryImpl(context, apiServer, ioExecutor, genreDao)
    }

    @JvmStatic
    @FragmentScope
    @Provides
    fun provideTrendRepository(apiServer:TMDBserver, ioExecutor: Executor, trendsDao:TrendsDao, preferences: SharedPreferences):TrendsRepository{
        return TrendsRepositoryImpl(trendsDao, apiServer, ioExecutor, preferences)
    }
}