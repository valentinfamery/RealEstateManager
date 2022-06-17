package com.openclassrooms.realestatemanager.repository

import android.app.Activity
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.models.PhotoWithText
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.utils.Resource
import com.openclassrooms.realestatemanager.utils.safeCall
import kotlinx.coroutines.*
import kotlinx.coroutines.tasks.await
import java.util.*

class RealEstateRepository {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

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
        activity: Activity,
    ) {


        val address3 = "1600 Amphitheatre Parkway, Mountain View, CA"

            val id = UUID.randomUUID().toString()



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
                null,
                null
            )
            usersCollection.document(id).set(realEstate)

        val storageRef = storage.reference

        //val realEstatesRef: StorageReference = storageRef.child("realEstates")

        //val realEstateRef: StorageReference = realEstatesRef.child("realEstates/$id")





        if(listPhotos!=null) {



            for (photoWithText in listPhotos) {

                runBlocking {
                    launch {
                        val urlFinal = uploadImageAndGetUrl(photoWithText.getPhotoUri()!!,id)

                        photoWithText.setUrl(urlFinal)
                    }
                }

                imagesCollectionRealEstates(id).document().set(photoWithText)
            }
        }


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



}

