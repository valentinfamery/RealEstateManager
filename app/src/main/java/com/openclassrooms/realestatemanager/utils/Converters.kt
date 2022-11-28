package com.openclassrooms.realestatemanager.utils

import android.net.Uri
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase

class Converters {

    @TypeConverter
    fun listToJsonString(value: List<PhotoWithTextFirebase>?): String = Gson().toJson(value)

    @TypeConverter
    fun jsonStringToList(value: String) = Gson().fromJson(value, Array<PhotoWithTextFirebase>::class.java).toList()
}
