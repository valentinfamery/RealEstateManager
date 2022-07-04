package com.openclassrooms.realestatemanager.models.resultGeocoding

import java.util.HashMap

data class Geometry (
    var bounds: Bounds? = null,
    var location: Location? = null,
    var locationType: String? = null,
    var viewport: Viewport? = null,
    val additionalProperties: MutableMap<String, Any> = HashMap()
)