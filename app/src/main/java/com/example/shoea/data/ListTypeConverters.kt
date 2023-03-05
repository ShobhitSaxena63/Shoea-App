package com.example.shoea.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListTypeConverters {
    @TypeConverter
    fun toJson(imageList: List<String>): String = Gson().toJson(imageList)

    @TypeConverter
    fun fromJson(string: String): List<String>  {
        val type = object: TypeToken<List<String>>() {}.type
        return Gson().fromJson(string, type)
    }

}