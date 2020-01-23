package com.example.moviedb.until

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type


class ListConverter {

    @TypeConverter
    fun fromList(list:List<Int>):String{
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(list:String):List<Int>{
        val listType: Type = object : TypeToken<ArrayList<Int?>?>() {}.type
        return Gson().fromJson(list, listType)
    }
}