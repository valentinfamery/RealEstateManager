package com.openclassrooms.realestatemanager.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.models.PhotoWithText
import com.openclassrooms.realestatemanager.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.User
import com.openclassrooms.realestatemanager.models.resultGeocoding.ResultGeocoding
import com.openclassrooms.realestatemanager.service.ApiInterface
import com.openclassrooms.realestatemanager.service.ApiService
import com.openclassrooms.realestatemanager.service.ApiService.`interface`
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class RealEstateRepository {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()
    private val mInterface: ApiInterface = ApiService.`interface`



    private val usersCollection: CollectionReference get() = FirebaseFirestore.getInstance().collection(COLLECTION_REAL_ESTATE)


    private fun imagesCollectionRealEstates(restaurantId: String): CollectionReference {
        return FirebaseFirestore.getInstance().collection(COLLECTION_REAL_ESTATE).document(restaurantId).collection(COLLECTION_REAL_ESTATE_IMAGES)
    }

    companion object {
        private const val COLLECTION_REAL_ESTATE = "real_estates"
        private const val COLLECTION_REAL_ESTATE_IMAGES = "real_estates_images"
    }

    val getRealEstates: MutableLiveData<List<RealEstate>> get() {
        val result : MutableLiveData<List<RealEstate>> = MutableLiveData<List<RealEstate>>()
        usersCollection.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                val userList = queryDocumentSnapshots.toObjects(
                    RealEstate::class.java
                )
            result.postValue(userList)

            }
            return result
        }

    fun getRealEstatePhotosWithId(id : String) : MutableLiveData<List<PhotoWithTextFirebase>> {
        val result : MutableLiveData<List<PhotoWithTextFirebase>> = MutableLiveData<List<PhotoWithTextFirebase>>()

        imagesCollectionRealEstates(id).get().addOnSuccessListener {queryDocumentSnapshots: QuerySnapshot ->
            val listPhotoWithText = queryDocumentSnapshots.toObjects(
                PhotoWithTextFirebase::class.java
            )
            result.postValue(listPhotoWithText)
        }

        return result
    }

    fun createRealEstate(
        type: String,
        price: Int,
        area: Int,
        numberRoom: Int,
        description: String,
        address: String,
        pointOfInterest: String,
        status: String,
        listPhotos: MutableList<PhotoWithText>? = null,
        dateEntry: String,
        dateSale: String,
    ) {


        val address3 = "1600 Amphitheatre Parkway, Mountain View, CA"

            val id = UUID.randomUUID().toString()


        val listRestaurantApiNearBySearchResponseOut: Call<ResultGeocoding?>? = `interface`.getResultGeocodingResponse(address3)

        listRestaurantApiNearBySearchResponseOut?.enqueue(object : Callback<ResultGeocoding?> {
            override fun onResponse(call: Call<ResultGeocoding?>, response: Response<ResultGeocoding?>) {

                if (response.body() != null) {

                    val lat = response.body()?.results?.get(0)?.geometry?.location?.lat
                    val lng = response.body()?.results?.get(0)?.geometry?.location?.lng
                    Log.e(lat.toString(),lng.toString())
                    Log.e("latlng",LatLng(lat!!, lng!!).toString())
                    val latLng = LatLng(lat, lng)
                    Log.e("result",latLng.toString())

                    val realEstate = RealEstate(
                        id,
                        type,
                        price,
                        area,
                        numberRoom,
                        description,
                        address3,
                        pointOfInterest,
                        status,
                        dateEntry,
                        dateSale,
                        firebaseAuth.currentUser?.displayName,
                        latLng.latitude,
                        latLng.longitude
                    )
                    usersCollection.document(id).set(realEstate)

                    if(listPhotos!=null) {
                        for (photoWithText in listPhotos) {
                            runBlocking {
                                launch {
                                    val urlFinal = uploadImageAndGetUrl(photoWithText.photoUri!!,id)

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

    fun deleteRealEstate(id : String) {
        usersCollection.document(id).delete()
    }

    fun getLatLngRealEstate(address: String) : LatLng?{
        var result :LatLng ?=null


        Log.e("result",result.toString())

        return result
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



