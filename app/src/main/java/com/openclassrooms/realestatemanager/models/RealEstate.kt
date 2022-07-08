package com.openclassrooms.realestatemanager.models

data class RealEstate (
    var id: String? = null,
    var type: String? = null,
    var price: Int? = null,
    var area: Int? = null,
    var numberRoom: Int? = null,
    var description: String? = null,
    var numberAndStreet: String? = null,
    var numberApartment: String? = null,
    var city: String? = null,
    var region: String? = null,
    var postalCode: String? = null,
    var country: String? = null,
    var status: String? = null,
    var dateOfEntry: String? = null,
    var dateOfSale: String? = null,
    var realEstateAgent: String? = null ,
    var lat: Double ?=null,
    var lng: Double ?=null,
    var hopitalNear : Boolean = false,
    var SchoolNear : Boolean = false,
    var shopsNear : Boolean = false,
    var parksNear : Boolean = false

)