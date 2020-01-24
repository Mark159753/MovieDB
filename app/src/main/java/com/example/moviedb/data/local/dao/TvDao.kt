package com.example.moviedb.data.local.dao

import androidx.paging.DataSource
import androidx.room.*
import com.example.moviedb.model.ResultMovie
import com.example.moviedb.model.ResultTV

@Dao
interface TvDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list:List<ResultTV>)

    @Query("SELECT * FROM resultTV")
    fun getMovies(): DataSource.Factory<Int, ResultTV>

    @Query("SELECT COUNT(*) FROM resultTV")
    fun getTableSize():Int

    @Query("DELETE FROM resultTV")
    fun clearMovies()
}