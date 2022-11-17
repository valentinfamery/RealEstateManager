package com.openclassrooms.realestatemanager.presentation.viewModels

import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.domain.models.PhotoWithText
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealEstateViewModel @Inject constructor(private val useCases: UseCases, private val realEstateRepository: RealEstateRepository) : ViewModel() {



    var createRealEstateResponse by mutableStateOf<Response<Boolean>>(Response.Empty)

    var list by mutableStateOf<Response<Boolean>>(Response.Empty)
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()


    val realEstates: StateFlow<List<RealEstateDatabase>> = realEstateRepository.realEstates().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), mutableListOf())

    init {
        refreshRealEstates()
    }

    fun refreshRealEstates() = viewModelScope.launch() {
        useCases.refreshRealEstates()
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
    
    fun getPropertyBySearch(supportSQLiteQuery: SupportSQLiteQuery): LiveData<List<RealEstateDatabase>> {
          return  realEstateRepository.getPropertyBySearch(supportSQLiteQuery)
    }


}