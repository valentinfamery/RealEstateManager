package com.openclassrooms.realestatemanager.domain.models

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class Estate(
    @PrimaryKey
    var id: String,
    var type: String? = null,
    var price: Int? = null,
    var area: Int? = null,
    var numberRoom: String? = null,
    var description: String? = null,
    var numberAndStreet: String? = null,
    var numberApartment: String? = null,
    var city: String? = null,
    var region: String? = null,
    var postalCode: String? = null,
    var country: String? = null,
    var status: String? = null,
    var dateOfEntry: String? = null,
    var dateOfSale: String? = null,
    var realEstateAgent: String? = null,
    var lat: Double ?=null,
    var lng: Double ?=null,
    var hospitalsNear : Boolean = false,
    var schoolsNear : Boolean = false,
    var shopsNear : Boolean = false,
    var parksNear : Boolean = false,
    @ColumnInfo(name = "listPhotoWithText")
    @Exclude
    var listPhotoWithText : List<Photo> ?=null,
    var count_photo : Int? = listPhotoWithText?.size,
): Parcelable {
    constructor() : this("",
        null,null,null,
    null,null,null,
        null,null,null,null,null,null,null,null,null,null,null,false,false,false,false)
    companion object NavigationType : NavType<Estate>(isNullableAllowed = false){
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
