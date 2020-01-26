package com.example.moviedb.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.example.moviedb.model.theathre.ResultMovie
import com.example.moviedb.model.trendsOfDay.TrendResult

@Dao
interface TrendsDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list:List<TrendResult>)

    @Query("SELECT * FROM trendsResult")
    fun getAllTrends(): LiveData<List<TrendResult>>

    @Query("SELECT * FROM trendsResult")
    fun getTrendsPagedList():DataSource.Factory<Int, TrendResult>

    @Query("SELECT * FROM trendsResult LIMIT :quantity")
    fun getTrendsOfQuantity(quantity:Int):LiveData<List<TrendResult>>

    @Query("SELECT COUNT(*) FROM trendsResult")
    fun getTableSize():Int

    @Query("DELETE FROM trendsResult")
    fun clearTrends()
}