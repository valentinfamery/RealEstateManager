package com.openclassrooms.realestatemanager.models.resultGeocoding

import java.util.HashMap

class Southwest {
    var lat: Double? = null
    var lng: Double? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}