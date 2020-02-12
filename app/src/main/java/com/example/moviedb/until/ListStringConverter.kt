package com.example.moviedb.until

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class ListStringConverter {

    @TypeConverter
    fun fromList(list:List<String>?):String?{
        val gson = Gson()
        return if (list == null) gson.toJson(list) else null
    }

    @TypeConverter
    fun toList(list:String?):List<String>?{
        val listType: Type = object : TypeToken<ArrayList<String?>?>() {}.type
        return if (list != null) Gson().fromJson(list, listType) else null
    }
}