package com.example.moviedb.data.local.dao

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.example.moviedb.model.trends.TrendResult

@Dao
interface TrendsDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(list:List<TrendResult>)

    @Query("SELECT * FROM trendsResult")
    fun getAllTrends(): LiveData<List<TrendResult>>

    @Query("SELECT * FROM trendsResult")
    fun getTrendsPagedList():DataSource.Factory<Int, TrendResult>

    @Query("SELECT * FROM trendsResult WHERE mediaType = :mediaType LIMIT :quantity")
    fun getTrendsOfQuantity(quantity:Int, mediaType: String):LiveData<List<TrendResult>>

    @Query("SELECT * FROM trendsResult WHERE mediaType = :mediaType")
    fun getTrendsOfType(mediaType:String):DataSource.Factory<Int, TrendResult>

    @Query("SELECT COUNT(*) FROM trendsResult")
    fun getTableSize():Int

    @Query("SELECT COUNT(*) FROM trendsResult WHERE mediaType = :mediaType")
    fun getSizeOfMediaType(mediaType: String):Int

    @Query("DELETE FROM trendsResult")
    fun clearTrends()

    @Query("DELETE FROM trendsResult WHERE mediaType = :mediaType")
    fun deleteTrendsOfType(mediaType: String)
}