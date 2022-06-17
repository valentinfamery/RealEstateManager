package com.openclassrooms.realestatemanager.models.resultGeocoding

import java.util.HashMap

class Geometry {
    var bounds: Bounds? = null
    var location: Location? = null
    var locationType: String? = null
    var viewport: Viewport? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}