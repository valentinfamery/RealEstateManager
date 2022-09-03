package com.openclassrooms.realestatemanager.models

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoWithTextFirebase(
    var photoUrl : String? = null,
    var text: String? =null
): Parcelable {
    companion object NavigationType : NavType<RealEstate>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RealEstate? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): RealEstate {
            return Gson().fromJson(value, RealEstate::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: RealEstate) {
            bundle.putParcelable(key, value)
        }
    }
}