package com.example.moviedb.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviedb.data.local.dao.*
import com.example.moviedb.model.genre.Genre
import com.example.moviedb.model.popular.PopularResult
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.model.trends.KnownFor
import com.example.moviedb.model.trends.TrendResult
import com.example.moviedb.model.tv.ResultTV
import com.example.moviedb.until.ListIntConverter
import com.example.moviedb.until.ListStringConverter

@Database(entities = [ResultMovie::class,
    ResultTV::class,
    PopularResult::class,
    TrendResult::class,
    Genre::class,
    KnownFor::class], version = 3, exportSchema = true)
@TypeConverters(ListIntConverter::class, ListStringConverter::class)
abstract class FilmsDB: RoomDatabase() {

    abstract fun getMoviesDao():MoviesDao

    abstract fun getTvDao():TvDao

    abstract fun getPopularMoviesDao():PopularMoviesDao

    abstract fun getTrendsDao():TrendsDao

    abstract fun getGenreDao():GenreDao

    abstract fun getPersonMoviesDao():PersonMoviesDao

    companion object{
        @Volatile
        private var instance:FilmsDB? = null

        operator fun invoke(context: Context) = instance ?: synchronized(this){
            instance ?: buildDataBase(context).also{ instance = it}
        }

        private fun buildDataBase(context: Context) = Room.databaseBuilder(
            context, FilmsDB::class.java, "FilmsDB"
        ).fallbackToDestructiveMigration()
            .build()
    }
}