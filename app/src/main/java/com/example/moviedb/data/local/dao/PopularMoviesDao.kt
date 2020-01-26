package com.example.moviedb.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviedb.model.popular.PopularResult

@Dao
interface PopularMoviesDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list:List<PopularResult>)

    @Query("DELETE FROM popularmovies")
    fun clearAll()

    @Query("SELECT * FROM popularmovies")
    fun getAllPopularMovies():LiveData<List<PopularResult>>
}