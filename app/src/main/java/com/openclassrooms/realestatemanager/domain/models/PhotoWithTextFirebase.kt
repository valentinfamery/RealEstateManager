package com.openclassrooms.realestatemanager.domain.models

import android.os.Bundle
import android.os.Parcelable
import androidx.compose.runtime.MutableState
import androidx.navigation.NavType
import com.google.firebase.database.Exclude
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class PhotoWithTextFirebase(
    var photoSource : String = "",
    var text: String ="",
    var id: String = "",
    @Exclude
    var toAddLatter: Boolean = false,
    @Exclude
    var toDeleteLatter: Boolean = false,
    @Exclude
    var toUpdateLatter: Boolean = false,

    ): Parcelable  {

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