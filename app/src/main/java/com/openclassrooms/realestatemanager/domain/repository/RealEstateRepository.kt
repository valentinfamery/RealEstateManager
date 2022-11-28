package com.openclassrooms.realestatemanager.domain.repository

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.domain.models.*
import kotlinx.coroutines.flow.Flow

interface RealEstateRepository {

    suspend fun refreshRealEstatesFromFirestore()

    fun realEstates() : Flow<List<RealEstateDatabase>>

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
        listPhotos: MutableList<PhotoWithTextFirebase>?,
        dateEntry: String,
        dateSale: String,
        realEstateAgent: String,
        checkedStateHospital : Boolean,
        checkedStateSchool : Boolean,
        checkedStateShops : Boolean,
        checkedStateParks : Boolean
    ) : Response<Boolean>

    fun getPropertyBySearch(
        type: String,
        city: String,
        minSurface: Int,
        maxSurface: Int,
        minPrice: Int,
        maxPrice: Int,
        onTheMarketLessALastWeek: Boolean,
        soldOn3LastMonth: Boolean,
        min3photos: Boolean,
        schools: Boolean,
        shops: Boolean
    ): LiveData<List<RealEstateDatabase>>

    fun realEstateById(realEstateId: String): LiveData<RealEstateDatabase?>



}








