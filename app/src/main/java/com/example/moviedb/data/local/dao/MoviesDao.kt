package com.example.moviedb.data.local.dao

import androidx.paging.DataSource
import androidx.room.*
import com.example.moviedb.model.theathre.ResultMovie

@Dao
interface MoviesDao{

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list:List<ResultMovie>)

    @Query("SELECT * FROM resultmovies")
    fun getMovies():DataSource.Factory<Int, ResultMovie>

    @Query("SELECT COUNT(*) FROM resultmovies")
    fun getTableSize():Int

    @Query("DELETE FROM resultmovies")
    fun cleareMovies()
}