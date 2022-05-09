package com.openclassrooms.realestatemanager.models

import android.media.Image
import java.util.*

class RealEstate(
    private var type: String? = null,
    private var price: Int? = null,
    private var area: Int? = null,
    private var numberRoom: Int? = null,
    private var description: String? = null,
    private var address: String? = null,
    private var pointsOfInterest: String? = null,
    private var status: String? = null,
    private var dateOfEntry: Date? = null,
    private var dateOfSale: Date? = null,
    private var realEstateAgent: String? = null,
    private var listPicture: Image? = null
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

    fun getDateOfEntry(): Date? {
        return dateOfEntry
    }

    fun getDateOfSale(): Date? {
        return dateOfSale
    }

    fun getRealEstateAgent(): String? {
        return realEstateAgent
    }

    fun getListPicture(): Image? {
        return listPicture
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

    fun setDateOfEntry(dateOfEntry: Date?) {
        this.dateOfEntry = dateOfEntry
    }

    fun setDateOfSale(dateOfSale: Date?) {
        this.dateOfSale = dateOfSale
    }

    fun setRealEstateAgent(realEstateAgent: String?) {
        this.realEstateAgent = realEstateAgent
    }

    fun setListPicture(listPicture: Image?) {
        this.listPicture = listPicture
    }

}