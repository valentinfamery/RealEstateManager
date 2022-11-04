package com.openclassrooms.realestatemanager.data.repository

import android.content.Context
import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.*
import com.openclassrooms.realestatemanager.domain.models.resultGeocoding.ResultGeocoding
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.service.ApiService
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import retrofit2.Call
import retrofit2.Callback
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RealEstateRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageRef : StorageReference,
    private val context: Context,
    private val realEstateDao: RealEstateDao): RealEstateRepository {


    override fun realEstates() : Flow<List<RealEstateDatabase>> {
        return realEstateDao.realEstates().flowOn(Dispatchers.IO)
    }

    override suspend fun refreshRealEstatesFromFirestore() {


            Log.e("items", "repo1")

            val isNetWorkAvailable = Utils.isInternetAvailable(context)

            if(isNetWorkAvailable){



                val realEstates = firebaseFirestore.collection("real_estates").get().await().map {
                    it.toObject(RealEstate::class.java)
                }

                realEstateDao.clear()

                for (realEstate in realEstates) {

                    Log.e("items","repo2")

                    val realEstateDatabase = RealEstateDatabase(
                        realEstate.id!!,
                        realEstate.type!!,
                        realEstate.price!!,
                        realEstate.area!!,
                        realEstate.numberRoom!!,
                        realEstate.description!!,
                        realEstate.numberAndStreet!!,
                        realEstate.numberApartment!!,
                        realEstate.city!!,
                        realEstate.region!!,
                        realEstate.postalCode!!,
                        realEstate.country!!,
                        realEstate.status!!,
                        realEstate.dateOfEntry!!,
                        realEstate.dateOfSale!!,
                        realEstate.realEstateAgent!!,
                        realEstate.lat!!,
                        realEstate.lng!!,
                        realEstate.hospitalsNear,
                        realEstate.schoolsNear,
                        realEstate.shopsNear,
                        realEstate.parksNear,
                        realEstate.listPhotoWithText,
                    )
                    realEstateDao.insertRealEstate(realEstateDatabase)
                }
            }



    }

    override suspend fun createRealEstate(
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
        listPhotos: MutableList<PhotoWithText>?,
        dateEntry: String,
        dateSale: String,
        realEstateAgent: String,
        checkedStateHospital : Boolean,
        checkedStateSchool : Boolean,
        checkedStateShops : Boolean,
        checkedStateParks : Boolean
    ) : Response<Boolean> {

        return try {
            Response.Loading

            val address3 = "$numberAndStreet,$city,$region"
            val id = UUID.randomUUID().toString()
            val listRestaurantApiNearBySearchResponseOut: Call<ResultGeocoding?>? =
                ApiService.`interface`.getResultGeocodingResponse(address3)

            listRestaurantApiNearBySearchResponseOut?.enqueue(object : Callback<ResultGeocoding?> {
                override fun onResponse(
                    call: Call<ResultGeocoding?>,
                    response: retrofit2.Response<ResultGeocoding?>
                ) {

                    if (response.body() != null) {


                        val lat = response.body()?.results?.get(0)?.geometry?.location?.lat
                        val lng = response.body()?.results?.get(0)?.geometry?.location?.lng
                        Log.e(lat.toString(), lng.toString())
                        Log.e("latLng", LatLng(lat!!, lng!!).toString())
                        val latLng = LatLng(lat, lng)
                        Log.e("result", latLng.toString())

                        val listPhotoWithTextFirebaseFinal: MutableList<PhotoWithTextFirebase> =
                            mutableListOf()

                        if (listPhotos != null) {
                            for (photoWithText in listPhotos) {
                                runBlocking {
                                    launch {
                                        val realEstateImage: StorageReference = storageRef.child(
                                            "realEstates/$id/" + UUID.randomUUID().toString()
                                        )

                                        val urlFinal = withContext(Dispatchers.IO) {
                                            realEstateImage.putFile(photoWithText.photoUri!!)
                                                .await().storage.downloadUrl.await()
                                        }.toString()
                                        Log.e("urlFinal", urlFinal)
                                        val photoWithTextFirebase =
                                            PhotoWithTextFirebase(urlFinal, photoWithText.text)
                                        listPhotoWithTextFirebaseFinal.add(photoWithTextFirebase)
                                    }
                                }
                            }
                        }

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
                            checkedStateParks,
                            listPhotoWithTextFirebaseFinal
                        )
                        firebaseFirestore.collection("real_estates").document(id).set(realEstate)


                    }
                }

                override fun onFailure(call: Call<ResultGeocoding?>, t: Throwable) {

                }
            })

            Response.Success(true)

        } catch (e: Exception) {
            Response.Failure(e)
        }

    }

}