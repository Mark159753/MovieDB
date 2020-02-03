package com.example.moviedb.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.moviedb.model.genre.Genre

@Dao
interface GenreDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list: List<Genre>)

    @Query("SELECT COUNT(*) FROM Genre")
    fun getTableSize():Int

    @Query("SELECT * FROM Genre")
    fun getAllGanres():LiveData<List<Genre>>
}