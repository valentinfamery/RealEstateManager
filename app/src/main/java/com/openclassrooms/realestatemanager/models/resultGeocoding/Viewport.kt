package com.openclassrooms.realestatemanager.models.resultGeocoding

import com.openclassrooms.realestatemanager.models.resultGeocoding.Northeast__1
import com.openclassrooms.realestatemanager.models.resultGeocoding.Southwest__1
import java.util.HashMap

class Viewport {
    var northeast: Northeast__1? = null
    var southwest: Southwest__1? = null
    private val additionalProperties: MutableMap<String, Any> = HashMap()
    fun getAdditionalProperties(): Map<String, Any> {
        return additionalProperties
    }

    fun setAdditionalProperty(name: String, value: Any) {
        additionalProperties[name] = value
    }
}