package com.openclassrooms.realestatemanager.domain.models.resultGeocoding

import java.util.HashMap

data class AddressComponent (
    var longName: String? = null,
    var shortName: String? = null,
    var types: List<String>? = null,
    val additionalProperties: MutableMap<String, Any> = HashMap()
)