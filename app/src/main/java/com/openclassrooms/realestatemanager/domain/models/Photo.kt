package com.openclassrooms.realestatemanager.domain.models

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.firebase.database.Exclude
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class Photo(
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

    companion object NavigationType : NavType<Estate>(isNullableAllowed = false) {
        override fun get(bundle: Bundle, key: String): Estate? {
            return bundle.getParcelable(key)
        }

        override fun parseValue(value: String): Estate {
            return Gson().fromJson(value, Estate::class.java)
        }

        override fun put(bundle: Bundle, key: String, value: Estate) {
            bundle.putParcelable(key, value)
        }
    }

}