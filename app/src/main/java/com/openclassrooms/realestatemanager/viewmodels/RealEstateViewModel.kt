package com.openclassrooms.realestatemanager.viewmodels

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RealEstateViewModel : ViewModel() {
    val realEstateRepository : RealEstateRepository = RealEstateRepository()

    val getRealEstates: MutableLiveData<List<RealEstate>> get() = realEstateRepository.getRealEstates

    fun createRealEstate(type: String, price: Int, area: Int, numberRoom: Int, description: String, address: String, pointOfInterest: String, status: String,listPhotosUri : List<Uri>,dateEntry : String ,dateSale :String
    ) {
        viewModelScope.launch(Dispatchers.Main) {
            realEstateRepository.createRealEstate(type , price , area , numberRoom , description , address , pointOfInterest , status,listPhotosUri,dateEntry,dateSale)
        }

    }

    fun deleteRealEstate(idRealEstate : String){
        realEstateRepository.deleteRealEstate(idRealEstate)
    }



}