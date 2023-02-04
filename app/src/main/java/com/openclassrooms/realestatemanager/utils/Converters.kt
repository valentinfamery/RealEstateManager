package com.openclassrooms.realestatemanager.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.domain.models.Photo

class Converters {

    @TypeConverter
    fun listToJsonString(value: List<Photo>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<Photo>::class.java).toList()
}
