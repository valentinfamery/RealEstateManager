package com.openclassrooms.realestatemanager.domain.models.resultGeocoding

import java.util.HashMap

data class Viewport (
    var northeast: Northeast__1? = null,
    var southwest: Southwest__1? = null,
    val additionalProperties: MutableMap<String, Any> = HashMap()
)