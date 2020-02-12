package com.example.moviedb.data.local.dao

import androidx.room.*
import com.example.moviedb.model.trends.KnownFor

@Dao
interface PersonMoviesDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list:List<KnownFor>)

    @Query("SELECT * FROM Person_Movies")
    fun getAllPersonMovies():List<KnownFor>

    @Query("SELECT * FROM Person_Movies WHERE personId = :userId")
    fun getMoviesForPerson(userId:Int):List<KnownFor>
}