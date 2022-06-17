package com.openclassrooms.realestatemanager.models.resultGeocoding

import com.openclassrooms.realestatemanager.models.resultGeocoding.AddressComponent
import com.openclassrooms.realestatemanager.models.resultGeocoding.Geometry
import java.util.HashMap

class Result {
    var addressComponents: List<AddressComponent>? = null
    var formattedAddress: String? = null
    var geometry: Geometry? = null
    var placeId: String? = null
    var types: List<String>? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}