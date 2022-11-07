package com.openclassrooms.realestatemanager.presentation.viewModels

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.models.PhotoWithText
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.use_case.UseCases
import com.openclassrooms.realestatemanager.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealEstateViewModel @Inject constructor(private val useCases: UseCases, private val realEstateRepository: RealEstateRepository) : ViewModel() {



    private val _realEstates = MutableLiveData<List<RealEstateDatabase>>()
    val realEstates: LiveData<List<RealEstateDatabase>> = _realEstates

    var createRealEstateResponse by mutableStateOf<Response<Boolean>>(Response.Empty)

    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()








    init {
        viewModelScope.launch {
            useCases.getRealEstates()
        }
        viewModelScope.launch{
            realEstateRepository.realEstates().collect{
                _realEstates.value = it
            }
        }

        viewModelScope.launch {
            realEstateRepository.NetworkChangeAlert().collect{
                Log.e("offlineMode",it.toString())
            }
        }
    }

    fun refreshRealEstates() = viewModelScope.launch() {
        useCases.getRealEstates()
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
    ) = viewModelScope.launch {
        createRealEstateResponse = useCases.createRealEstate(type , price , area , numberRoom , description , numberAndStreet,
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