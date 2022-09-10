package com.openclassrooms.realestatemanager.domain.use_case

import com.openclassrooms.realestatemanager.domain.models.PhotoWithText
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository

class CreateRealEstate(private val repo: RealEstateRepository) {
    suspend operator fun invoke(
        type: String,
        price: String,
        area: String,
        numberRoom: String,
        description: String,
        numberAndStreet: String,
        numberApartment: String,
        city: String,
        region: String,
        postalCode: String,
        country: String,
        status: String,
        listPhotos: MutableList<PhotoWithText>?,
        dateEntry: String,
        dateSale: String,
        realEstateAgent: String,
        checkedStateHospital : Boolean,
        checkedStateSchool : Boolean,
        checkedStateShops : Boolean,
        checkedStateParks : Boolean)

    = repo.createRealEstate(
        type,
        price,
        area,
        numberRoom,
        description,
        numberAndStreet,
        numberApartment,
        city,
        region,
        postalCode,
        country,
        status,
        listPhotos,
        dateEntry,
        dateSale,
        realEstateAgent,
        checkedStateHospital,
        checkedStateSchool,
        checkedStateShops,
        checkedStateParks
    )
}