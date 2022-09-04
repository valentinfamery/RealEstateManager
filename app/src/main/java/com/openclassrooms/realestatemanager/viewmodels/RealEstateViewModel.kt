package com.openclassrooms.realestatemanager.viewmodels

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.RealEstateRoomDatabase
import com.openclassrooms.realestatemanager.models.PhotoWithText
import com.openclassrooms.realestatemanager.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.utils.Resource
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch

class RealEstateViewModel(application: Application) : ViewModel() {
    private val realEstateRepository : RealEstateRepository


    init {
        val database = RealEstateRoomDatabase.getInstance(application)
        val realEstateDao = database.realEstateDao()
        realEstateRepository = RealEstateRepository(realEstateDao)
    }

    fun uiState(context: Context) :  StateFlow<Resource<List<RealEstateDatabase>>>{
        return flow {
            emit(realEstateRepository.fetchRealEstates(context))
        }.stateIn(
            scope = viewModelScope,
            started = WhileSubscribed(5000), // Or Lazily because it's a one-shot
            initialValue = Resource.Loading()
        )
    }

    fun refreshRealEstates(context: Context){
        viewModelScope.launch {
            realEstateRepository.fetchRealEstates(context)
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