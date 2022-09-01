package com.openclassrooms.realestatemanager.viewmodels

import android.app.Application
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
import kotlinx.coroutines.launch

class RealEstateViewModel(application: Application) : ViewModel() {
    private val realEstateRepository : RealEstateRepository


    private val _myUiState = MutableStateFlow<Resource<List<RealEstateDatabase>>>(Resource.Loading())
    val uiState: StateFlow<Resource<List<RealEstateDatabase>>> = _myUiState

    init {
        val database = RealEstateRoomDatabase.getInstance(application)
        val realEstateDao = database.realEstateDao()
        realEstateRepository = RealEstateRepository(realEstateDao)
        viewModelScope.launch {
            _myUiState.value = realEstateRepository.fetchRealEstates()
        }
    }




    fun refreshRealEstates(){
        viewModelScope.launch {
            realEstateRepository.fetchRealEstates()
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

    fun getRealEstatePhotosWithId(id : String):StateFlow<List<PhotoWithTextFirebase>>{
        val _uiState = MutableStateFlow(listOf<PhotoWithTextFirebase>())
        val uiState: StateFlow<List<PhotoWithTextFirebase>> = _uiState

        viewModelScope.launch {
            // Trigger the flow and consume its elements using collect
            realEstateRepository.getRealEstatePhotosWithId(id).collect { favoriteNews ->
                _uiState.value = favoriteNews
                // Update View with the latest favorite news
            }
        }

       return uiState
    }


    fun getRealEstateById(id : String): MutableLiveData<RealEstate?> {
        return realEstateRepository.getRealEstateById(id)
    }



}