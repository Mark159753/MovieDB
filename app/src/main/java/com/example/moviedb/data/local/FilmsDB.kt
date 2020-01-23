package com.example.moviedb.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.moviedb.data.local.dao.MoviesDao
import com.example.moviedb.model.ResultMovie
import com.example.moviedb.until.ListConverter

@Database(entities = [ResultMovie::class], version = 1, exportSchema = false)
@TypeConverters(ListConverter::class)
abstract class FilmsDB: RoomDatabase() {

    abstract fun getMoviesDao():MoviesDao

    companion object{
        @Volatile
        private var instance:FilmsDB? = null

        operator fun invoke(context: Context) = instance ?: synchronized(this){
            instance ?: buildDataBase(context).also{ instance = it}
        }

        private fun buildDataBase(context: Context) = Room.databaseBuilder(
            context, FilmsDB::class.java, "FilmsDB"
        ).build()
    }
}