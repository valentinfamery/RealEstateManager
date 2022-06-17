package com.openclassrooms.realestatemanager.models


class RealEstate(
    private var id: String? = null,
    private var type: String? = null,
    private var price: Int? = null,
    private var area: Int? = null,
    private var numberRoom: Int? = null,
    private var description: String? = null,
    private var address: String? = null,
    private var pointsOfInterest: String? = null,
    private var status: String? = null,
    private var dateOfEntry: String? = null,
    private var dateOfSale: String? = null,
    private var realEstateAgent: String? = null ,
    private var lat: Double? = null,
    private var lng: Double? = null

) {
    fun getType(): String? {
        return type
    }

    fun getPrice(): Int? {
        return price
    }

    fun getArea(): Int? {
        return area
    }

    fun getNumberRoom(): Int? {
        return numberRoom
    }

    fun getDescription(): String? {
        return description
    }

    fun getAddress(): String? {
        return address
    }

    fun getPointsOfInterest(): String? {
        return pointsOfInterest
    }

    fun getStatus(): String? {
        return status
    }

    fun getDateOfEntry(): String? {
        return dateOfEntry
    }

    fun getDateOfSale(): String? {
        return dateOfSale
    }

    fun getRealEstateAgent(): String? {
        return realEstateAgent
    }

    fun getId(): String? {
        return id
    }

    fun setType(type: String?) {
        this.type = type
    }

    fun setPrice(price: Int?) {
        this.price = price
    }

    fun setArea(area: Int?) {
        this.area = area
    }

    fun setNumberRoom(numberRoom: Int?) {
        this.numberRoom = numberRoom
    }

    fun setDescription(description: String?) {
        this.description = description
    }

    fun setAddress(address: String?) {
        this.address = address
    }

    fun setPointsOfInterest(pointsOfInterest: String?) {
        this.pointsOfInterest = pointsOfInterest
    }

    fun setStatus(status: String?) {
        this.status = status
    }

    fun setDateOfEntry(dateOfEntry: String?) {
        this.dateOfEntry = dateOfEntry
    }

    fun setDateOfSale(dateOfSale: String?) {
        this.dateOfSale = dateOfSale
    }

    fun setRealEstateAgent(realEstateAgent: String?) {
        this.realEstateAgent = realEstateAgent
    }

    fun setId(id: String?) {
        this.id = id
    }

}