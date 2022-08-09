package com.openclassrooms.realestatemanager.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.PhotoWithText
import com.openclassrooms.realestatemanager.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RealEstateViewModel : ViewModel() {
    private val realEstateRepository : RealEstateRepository = RealEstateRepository()

    private val _uiState = MutableStateFlow(listOf<RealEstate>())
    val uiState: StateFlow<List<RealEstate>> = _uiState

    init {
        viewModelScope.launch {
            // Trigger the flow and consume its elements using collect
            realEstateRepository.latestRealEstates.collect { favoriteNews ->
                _uiState.value = favoriteNews
                // Update View with the latest favorite news
            }
        }
    }

    fun createRealEstate(
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
        listPhotosUri: MutableList<PhotoWithText>,
        dateEntry: String,
        dateSale: String,
        realEstateAgent:String,
        checkedStateHospital : Boolean,
        checkedStateSchool : Boolean,
        checkedStateShops : Boolean,
        checkedStateParks : Boolean
    ) {
        realEstateRepository.createRealEstate(type , price , area , numberRoom , description , numberAndStreet,
            numberApartment,
            city,
            region,
            postalCode,
            country, status,listPhotosUri,dateEntry,dateSale,realEstateAgent,checkedStateHospital,
            checkedStateSchool,
            checkedStateShops,
            checkedStateParks)
    }

    fun getRealEstatePhotosWithId(id : String):MutableLiveData<List<PhotoWithTextFirebase>>{
       return realEstateRepository.getRealEstatePhotosWithId(id)
    }

    fun getRealEstateById(id : String): MutableLiveData<RealEstate?> {
        return realEstateRepository.getRealEstateById(id)
    }



}