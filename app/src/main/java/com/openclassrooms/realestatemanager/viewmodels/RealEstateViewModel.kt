package com.openclassrooms.realestatemanager.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.RealEstateRoomDatabase
import com.openclassrooms.realestatemanager.models.PhotoWithText
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class RealEstateViewModel(application: Application) : ViewModel() {
    private val realEstateRepository : RealEstateRepository


    init {
        val database = RealEstateRoomDatabase.getInstance(application)
        val realEstateDao = database.realEstateDao()
        realEstateRepository = RealEstateRepository(realEstateDao)
    }

    private val _myUiState = MutableStateFlow<Resource<List<RealEstateDatabase>>>(Resource.Loading())

    fun uiState(isNetWorkAvailable : Boolean) : StateFlow<Resource<List<RealEstateDatabase>>> {
        viewModelScope.launch {
            val result = realEstateRepository.fetchRealEstates(isNetWorkAvailable)
            _myUiState.value = result
        }
        return _myUiState
    }

    fun refreshRealEstates(isNetWorkAvailable : Boolean){
        viewModelScope.launch {
            _myUiState.value = realEstateRepository.fetchRealEstates(isNetWorkAvailable)
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





}