package com.openclassrooms.realestatemanager.models.resultGeocoding

import java.util.HashMap

data class Southwest (
    var lat: Double? = null,
    var lng: Double? = null,
    val additionalProperties: MutableMap<String, Any> = HashMap()
)