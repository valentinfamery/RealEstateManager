package com.openclassrooms.realestatemanager.domain.repository

import com.openclassrooms.realestatemanager.domain.models.*
import kotlinx.coroutines.flow.Flow

interface RealEstateRepository {

    suspend fun getRealEstatesFromFirestore() : Response<List<RealEstateDatabase>>

    suspend fun createRealEstate(
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
        checkedStateParks : Boolean
    ) : Flow<Response<Void?>>



}








