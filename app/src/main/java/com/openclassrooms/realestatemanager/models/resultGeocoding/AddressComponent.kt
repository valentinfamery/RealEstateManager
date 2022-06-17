package com.openclassrooms.realestatemanager.models.resultGeocoding

import java.util.HashMap

class AddressComponent {
    var longName: String? = null
    var shortName: String? = null
    var types: List<String>? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}