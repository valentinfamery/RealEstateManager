package com.openclassrooms.realestatemanager.models

data class RealEstate (
    var id: String? = null,
    var type: String? = null,
    var price: Int? = null,
    var area: Int? = null,
    var numberRoom: Int? = null,
    var description: String? = null,
    var address: String? = null,
    var pointsOfInterest: String? = null,
    var status: String? = null,
    var dateOfEntry: String? = null,
    var dateOfSale: String? = null,
    var realEstateAgent: String? = null ,
    var lat: Double? = null,
    var lng: Double? = null
)