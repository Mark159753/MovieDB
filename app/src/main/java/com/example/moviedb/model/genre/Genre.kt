package com.example.moviedb.model.genre


import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize as Parcelize1

@Entity(tableName = "Genre")
@Parcelize1
data class Genre(
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val name: String
): Parcelable {
    @Ignore
    var IsSelected:Boolean = false
}