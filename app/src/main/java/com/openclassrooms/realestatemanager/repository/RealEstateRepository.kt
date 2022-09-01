package com.openclassrooms.realestatemanager.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.models.*
import com.openclassrooms.realestatemanager.models.resultGeocoding.ResultGeocoding
import com.openclassrooms.realestatemanager.service.ApiService.`interface`
import com.openclassrooms.realestatemanager.utils.Resource
import com.openclassrooms.realestatemanager.utils.safeCall
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class RealEstateRepository(private val realEstateDao: RealEstateDao) {

    private val storage = FirebaseStorage.getInstance()
    private val usersCollection: CollectionReference get() = FirebaseFirestore.getInstance().collection(COLLECTION_REAL_ESTATE)

    private fun imagesCollectionRealEstates(restaurantId: String): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_REAL_ESTATE).document(restaurantId).collection(COLLECTION_REAL_ESTATE_IMAGES)
    }

    companion object {
        const val COLLECTION_REAL_ESTATE = "real_estates"
        private const val COLLECTION_REAL_ESTATE_IMAGES = "real_estates_images"
    }

    fun getRealEstatePhotosWithId(id : String): Flow<List<PhotoWithTextFirebase>> = flow {
            val photosWithId = fetchPhotosWithId(id)
            emit(photosWithId)
    }

    suspend fun fetchRealEstates(): Resource<List<RealEstateDatabase>> {

                Log.e("items","repo1")
                val list = usersCollection.get().await().map { document ->
                    document.toObject(RealEstate::class.java)
                }


                Log.e("items1Repo1", list[0].city.toString())

                clearDataBase()

            for (item in list) {
                val realEstateDatabase = RealEstateDatabase(
                    item.id!!,
                    item.type!!,
                    item.price!!,
                    item.area!!,
                    item.numberRoom!!,
                    item.description!!,
                    item.numberAndStreet!!,
                    item.numberApartment!!,
                    item.city!!,
                    item.region!!,
                    item.postalCode!!,
                    item.country!!,
                    item.status!!,
                    item.dateOfEntry!!,
                    item.dateOfSale!!,
                    item.realEstateAgent!!,
                    item.lat!!,
                    item.lng!!,
                    item.hospitalsNear,
                    item.schoolsNear,
                    item.shopsNear,
                    item.parksNear
                )

                        insertRealEstate(realEstateDatabase)

        }
                Log.e("items","repo2")

        return Resource.Success(realEstateDao.realEstates())
    }

    private suspend fun clearDataBase(){
        realEstateDao.clear()
    }

    private suspend fun insertRealEstate(realEstateDatabase: RealEstateDatabase){
        realEstateDao.insertRealEstate(realEstateDatabase)
    }

    private suspend fun fetchPhotosWithId(id : String): List<PhotoWithTextFirebase> {
        val list = imagesCollectionRealEstates(id).get().await().map { document ->
            document.toObject(PhotoWithTextFirebase::class.java)
        }
        return list
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
        listPhotos: MutableList<PhotoWithText>? = null,
        dateEntry: String,
        dateSale: String,
        realEstateAgent: String,
        checkedStateHospital : Boolean,
        checkedStateSchool : Boolean,
        checkedStateShops : Boolean,
        checkedStateParks : Boolean
    ) {


        val address3 = "$numberAndStreet,$city,$region"

            val id = UUID.randomUUID().toString()


        val listRestaurantApiNearBySearchResponseOut: Call<ResultGeocoding?>? = `interface`.getResultGeocodingResponse(address3)

        listRestaurantApiNearBySearchResponseOut?.enqueue(object : Callback<ResultGeocoding?> {
            override fun onResponse(call: Call<ResultGeocoding?>, response: Response<ResultGeocoding?>) {

                if (response.body() != null) {

                    val lat = response.body()?.results?.get(0)?.geometry?.location?.lat
                    val lng = response.body()?.results?.get(0)?.geometry?.location?.lng
                    Log.e(lat.toString(),lng.toString())
                    Log.e("latLng",LatLng(lat!!, lng!!).toString())
                    val latLng = LatLng(lat, lng)
                    Log.e("result",latLng.toString())

                    val realEstate = RealEstate(
                        id,
                        type,
                        price,
                        area,
                        numberRoom,
                        description,
                        numberAndStreet,
                        numberApartment,
                        city,
                        region,
                        postalCode,
                        country,
                        status,
                        dateEntry,
                        dateSale,
                        realEstateAgent,
                        latLng.latitude,
                        latLng.longitude,
                        checkedStateHospital,
                        checkedStateSchool,
                        checkedStateShops,
                        checkedStateParks
                    )
                    usersCollection.document(id).set(realEstate)

                    if(listPhotos!=null) {
                        for (photoWithText in listPhotos) {
                            runBlocking {
                                launch {
                                    val urlFinal = uploadImageAndGetUrl(photoWithText.photoUri!!,id)

                                    Log.e("urlFinal",urlFinal)

                                    val photoWithTextFirebase = PhotoWithTextFirebase(urlFinal,photoWithText.text)

                                    imagesCollectionRealEstates(id).document().set(photoWithTextFirebase)

                                }
                            }
                        }
                    }


                }
            }

            override fun onFailure(call: Call<ResultGeocoding?>, t: Throwable) {

            }
        })











    }

    private suspend fun uploadImageAndGetUrl(uri :Uri,id:String) : String{
        val storageRef = storage.reference
        val realEstateImage : StorageReference = storageRef.child("realEstates/$id/"+UUID.randomUUID().toString())
        return withContext(Dispatchers.IO) {
            realEstateImage.putFile(uri).await().storage.downloadUrl.await()
        }.toString()
    }

    fun getRealEstateById(id : String): MutableLiveData<RealEstate?>{
        val result : MutableLiveData<RealEstate?> = MutableLiveData<RealEstate?>()

        usersCollection.document(id).get().addOnSuccessListener {
            usersCollection.document(id).get().addOnSuccessListener { documentSnapshot ->
                val realEstate = documentSnapshot.toObject(RealEstate::class.java)
                result.postValue(realEstate)
            }
        }
        return result

    }



}








