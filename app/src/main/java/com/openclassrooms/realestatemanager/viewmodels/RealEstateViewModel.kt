package com.openclassrooms.realestatemanager.viewmodels

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.models.PhotoWithText
import com.openclassrooms.realestatemanager.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.repository.RealEstateRepository

class RealEstateViewModel : ViewModel() {
    private val realEstateRepository : RealEstateRepository = RealEstateRepository()

    val getRealEstates: MutableLiveData<List<RealEstate>> get() = realEstateRepository.getRealEstates

    fun createRealEstate(
        type: String,
        price: Int,
        area: Int,
        numberRoom: Int,
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
        checkedStateHopital : Boolean,
        checkedStateSchool : Boolean,
        checkedStateShops : Boolean,
        checkedStateParks : Boolean
    ) {
        realEstateRepository.createRealEstate(type , price , area , numberRoom , description , numberAndStreet,
            numberApartment,
            city,
            region,
            postalCode,
            country, status,listPhotosUri,dateEntry,dateSale,realEstateAgent,checkedStateHopital,
            checkedStateSchool,
            checkedStateShops,
            checkedStateParks)
    }



    fun deleteRealEstate(idRealEstate : String){
        realEstateRepository.deleteRealEstate(idRealEstate)
    }

    fun getLatLngRealEstate(address : String){
       realEstateRepository.getLatLngRealEstate(address)
    }

    fun getRealEstatePhotosWithId(id : String):MutableLiveData<List<PhotoWithTextFirebase>>{
       return realEstateRepository.getRealEstatePhotosWithId(id)
    }

    fun getRealEstateById(id : String): MutableLiveData<RealEstate?> {
        return realEstateRepository.getRealEstateById(id)
    }

}