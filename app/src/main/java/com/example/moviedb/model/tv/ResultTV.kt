package com.example.moviedb.model.tv


import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.moviedb.until.ListStringConverter
import com.google.gson.annotations.SerializedName

@Entity(tableName = "resultTV")
data class ResultTV(
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("first_air_date")
    val firstAirDate: String,
    @SerializedName("genre_ids")
    @TypeConverters(ListStringConverter::class)
    val genreIds: List<Int>,
    val id: Int,
    val name: String,
    @SerializedName("origin_country")
    @TypeConverters(ListStringConverter::class)
    val originCountry: List<String>,
    @SerializedName("original_language")
    val originalLanguage: String,
    @SerializedName("original_name")
    val originalName: String,
    val overview: String,
    val popularity: Double,
    @SerializedName("poster_path")
    val posterPath: String?,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("vote_count")
    val voteCount: Int
){
    @PrimaryKey(autoGenerate = true)
    var primaryKey:Int = 0
}