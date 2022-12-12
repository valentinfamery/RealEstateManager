package com.openclassrooms.realestatemanager.domain.models

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoWithTextFirebase(
    var photoUri : String = "",
    var photoUrl : String = "",
    var text: String ="",
    var id: String = "",
    var toAddLatter: Boolean = false,
    var toDeleteLatter: Boolean = false,
    var toUpdateLatter: Boolean = false,

    ): Parcelable {
    companion object NavigationType : NavType<RealEstateDatabase>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): RealEstateDatabase? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): RealEstateDatabase {
            return Gson().fromJson(value, RealEstateDatabase::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: RealEstateDatabase) {
            bundle.putParcelable(key, value)
        }
    }
}