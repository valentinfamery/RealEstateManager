package com.openclassrooms.realestatemanager.domain.models.resultGeocoding

import java.util.HashMap

data class ResultGeocoding(
    var results: List<Result>? = null,
    var status: String? = null,
    val additionalProperties: MutableMap<String, Any> = HashMap()
)