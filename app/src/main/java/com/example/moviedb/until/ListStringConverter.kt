package com.example.moviedb.until

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ListStringConverter {

    @TypeConverter
    fun fromList(list:List<String>):String{
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun toList(list:String):List<String>{
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return Gson().fromJson(list, listType)
    }
}