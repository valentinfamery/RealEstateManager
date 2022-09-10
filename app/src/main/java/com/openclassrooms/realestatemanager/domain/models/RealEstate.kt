package com.openclassrooms.realestatemanager.domain.models

import android.os.Bundle
import android.os.Parcelable
import androidx.navigation.NavType
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
data class RealEstate (
    var id: String?=null,
    var type: String? = null,
    var price: String? = null,
    var area: String? = null,
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
    var listPhotoWithText : List<PhotoWithTextFirebase> ?=null,

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