package com.openclassrooms.realestatemanager.models.resultGeocoding

import java.util.HashMap

class ResultGeocoding {
    var results: List<Result>? = null
    var status: String? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}