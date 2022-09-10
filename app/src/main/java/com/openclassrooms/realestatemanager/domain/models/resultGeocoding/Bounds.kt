package com.openclassrooms.realestatemanager.domain.models.resultGeocoding

import java.util.HashMap

data class Bounds (
    var northeast: Northeast? = null,
    var southwest: Southwest? = null,
    val additionalProperties: MutableMap<String, Any> = HashMap()
)