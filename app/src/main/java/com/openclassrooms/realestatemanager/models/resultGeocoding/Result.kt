package com.openclassrooms.realestatemanager.models.resultGeocoding

import java.util.HashMap

data class Result(
    var addressComponents: List<AddressComponent>? = null,
    var formattedAddress: String? = null,
    var geometry: Geometry? = null,
    var placeId: String? = null,
    var types: List<String>? = null,
    val additionalProperties: MutableMap<String, Any> = HashMap()
)
