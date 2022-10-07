package com.openclassrooms.realestatemanager.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.domain.models.PhotoWithText
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@InternalCoroutinesApi
class RealEstateViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {

    var realEstatesResponse by mutableStateOf<Response<List<RealEstateDatabase>>>(Response.Loading)

    var createRealEstateResponse by mutableStateOf<Response<Void?>>(Response.Success(null))

    init {
        getRealEstates()
    }


    private fun getRealEstates() = viewModelScope.launch {
        useCases.getRealEstates().collect{ response ->
            realEstatesResponse = response
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
    ) = viewModelScope.launch {
        useCases.createRealEstate(type , price , area , numberRoom , description , numberAndStreet,
            numberApartment,
            city,
            region,
            postalCode,
            country, status,listPhotosUri,dateEntry,dateSale,realEstateAgent,checkedStateHospital,
            checkedStateSchool,
            checkedStateShops,
            checkedStateParks).collect{response ->
            createRealEstateResponse = response

        }
    }





}