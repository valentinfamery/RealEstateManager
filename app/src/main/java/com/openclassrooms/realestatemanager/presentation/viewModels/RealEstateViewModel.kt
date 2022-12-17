package com.openclassrooms.realestatemanager.presentation.viewModels

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.*
import com.openclassrooms.realestatemanager.domain.models.*
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RealEstateViewModel @Inject constructor(private val useCases: UseCases, private val realEstateRepository: RealEstateRepository) : ViewModel() {




    var createRealEstateResponse by mutableStateOf<Response<Boolean>>(Response.Empty)
    var updateRealEstateResponse by mutableStateOf<Response<Boolean>>(Response.Empty)

    var list by mutableStateOf<Response<Boolean>>(Response.Empty)
    private val _isRefreshing = MutableStateFlow(false)

    val isRefreshing: StateFlow<Boolean> get() = _isRefreshing.asStateFlow()

    val myUiState = MutableLiveData<List<PhotoWithTextFirebase>>()


    fun fillMyUiState(list : List<PhotoWithTextFirebase>){
        myUiState.value = list
    }

    fun addPhoto(photoWithTextFirebase: PhotoWithTextFirebase){
        myUiState.value = myUiState.value!! + photoWithTextFirebase
    }

    val realEstates: StateFlow<List<RealEstateDatabase>> = realEstateRepository.realEstates().stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), mutableListOf())

    fun updateAttribute(id: String) {
        myUiState.updateElement({ it.id == id }, {
            it.copy(toDeleteLatter = true)
        })
    }

    fun updateAttributeToUpdate(id: String) {
        myUiState.updateElement({ it.id == id && !it.toAddLatter }, {
            it.copy(toUpdateLatter = true)
        })
    }

    fun updateAttributePhotoSource(id: String,photoSource : String) {
        myUiState.updateElement({ it.id == id }, {
            it.copy(photoSource = photoSource)
        })
    }

    fun updateAttributePhotoText(id: String,text : String) {
        myUiState.updateElement({ it.id == id }, {
            it.copy(text = text)
        })
    }


    fun <T> MutableLiveData<List<T>>.updateElement(predicate: (T) -> Boolean, update: (T) -> T) {
        // Récupérer la valeur actuelle de la liste
        val currentValue = this.value ?: return

        // Appliquer la fonction update à tous les éléments de la liste qui satisfont le prédicat
        val updatedList = currentValue.map { element ->
            if (predicate(element)) update(element) else element
        }

        // Mettre à jour la liste avec la nouvelle valeur
        this.value = updatedList
    }

    fun refreshRealEstates() = viewModelScope.launch {
        useCases.refreshRealEstates()
    }

    fun realEstateById(realEstateId : String): LiveData<RealEstateDatabase?> {return realEstateRepository.realEstateById(realEstateId)}


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
        listPhotosUri: MutableList<PhotoWithTextFirebase>,
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
    ): LiveData<List<RealEstateDatabase>> {
          return  realEstateRepository.getPropertyBySearch(type,city,minSurface,maxSurface,minPrice,maxPrice,onTheMarketLessALastWeek,soldOn3LastMonth,min3photos,schools,shops)
    }

    fun updateRealEstate(
        id: String,
        entryType: String,
        entryPrice: String,
        entryArea: String,
        entryNumberRoom: String,
        entryDescription: String,
        entryNumberAndStreet: String,
        entryNumberApartement: String,
        entryCity: String,
        entryRegion: String,
        entryPostalCode: String,
        entryCountry: String,
        entryStatus: String,
        textDateOfEntry: String,
        textDateOfSale: String,
        realEstateAgent: String?,
        lat: Double?,
        lng: Double?,
        checkedStateHopital: MutableState<Boolean>,
        checkedStateSchool: MutableState<Boolean>,
        checkedStateShops: MutableState<Boolean>,
        checkedStateParks: MutableState<Boolean>,
        listPhotoWithText:MutableList<PhotoWithTextFirebase>,
        itemRealEstate: RealEstateDatabase
    ) = viewModelScope.launch {
        updateRealEstateResponse = realEstateRepository.updateRealEstate(id,
            entryType,
            entryPrice,
            entryArea,
            entryNumberRoom,
            entryDescription,
            entryNumberAndStreet,
            entryNumberApartement,
            entryCity,
            entryRegion,
            entryPostalCode,
            entryCountry,
            entryStatus,
            textDateOfEntry,
            textDateOfSale,
            realEstateAgent,
        lat,
        lng,
        checkedStateHopital,
        checkedStateSchool,
        checkedStateShops,
        checkedStateParks,
        listPhotoWithText,
        itemRealEstate)
    }


}