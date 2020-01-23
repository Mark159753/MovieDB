package com.example.moviedb.di.modules

import android.content.Context
import android.content.SharedPreferences
import androidx.fragment.app.FragmentActivity
import com.example.moviedb.data.local.dao.MoviesDao
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.data.repository.Repository
import com.example.moviedb.data.repository.RepositoryImpl
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
    fun provideRepository(dao: MoviesDao, server: TMDBserver, ioExecutor: Executor, preferences: SharedPreferences): Repository {
        return RepositoryImpl(dao, server, ioExecutor, preferences)
    }
}