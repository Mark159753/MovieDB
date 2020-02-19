package com.example.moviedb.di.modules

import android.content.Context
import android.content.SharedPreferences
import com.example.moviedb.data.local.FilmsDB
import com.example.moviedb.data.local.dao.*
import com.example.moviedb.data.network.TMDBserver
import com.example.moviedb.data.repository.Repository
import com.example.moviedb.data.repository.RepositoryImpl
import com.example.moviedb.data.repository.paging.PagingRepository
import com.example.moviedb.data.repository.paging.PagingRepositoryImpl
import com.example.moviedb.di.scope.FragmentScope
import dagger.Module
import dagger.Provides
import java.util.concurrent.Executor
import java.util.concurrent.Executors
import javax.inject.Singleton

@Module
object ApplicationModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideFilmsDB(context: Context):FilmsDB{
        return FilmsDB.invoke(context)
    }


    @JvmStatic
    @Singleton
    @Provides
    fun providePagingRepository(moviesDao: MoviesDao, tvDao: TvDao, context: Context,
                                server:TMDBserver, ioExecutor: Executor): PagingRepository {
        return PagingRepositoryImpl(moviesDao, tvDao, context, server, ioExecutor)
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideMoviesDao(database:FilmsDB):MoviesDao{
        return database.getMoviesDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideTvDao(database:FilmsDB):TvDao{
        return database.getTvDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providePopularMoviesDao(database:FilmsDB):PopularMoviesDao{
        return database.getPopularMoviesDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideTrendsDao(database:FilmsDB):TrendsDao{
        return database.getTrendsDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun provideGenreDao(database:FilmsDB):GenreDao{
        return database.getGenreDao()
    }

    @JvmStatic
    @Singleton
    @Provides
    fun providePersonMoviesDao(database:FilmsDB):PersonMoviesDao{
        return database.getPersonMoviesDao()
    }


    @JvmStatic
    @Singleton
    @Provides
    fun provideTMDBserver():TMDBserver{
        return TMDBserver.invoke()
    }

    @JvmStatic
    @Provides
    fun provideIoExecutor():Executor{
        return Executors.newSingleThreadExecutor()
    }

}
