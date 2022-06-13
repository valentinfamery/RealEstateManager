package com.openclassrooms.realestatemanager.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.models.PhotoWithText
import com.openclassrooms.realestatemanager.models.RealEstate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
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
        listPhotos: List<PhotoWithText>,
        dateEntry: String,
        dateSale: String,
        viewModelScope: CoroutineScope,
    ) {


            val id = UUID.randomUUID().toString()
        var listUrlPhotos : List<PhotoWithText> ?=null


        viewModelScope.launch{
            listUrlPhotos = addAllImagesAndGetUrls(listPhotos)
        }





            val realEstate = RealEstate(
                id,
                type,
                price,
                area,
                numberRoom,
                description,
                address,
                pointOfInterest,
                status,
                dateEntry,
                dateSale,
                firebaseAuth.currentUser?.displayName,
                listUrlPhotos
            )
            usersCollection.document(id).set(realEstate)

        if(listUrlPhotos !=null) {

            for (photoWithText in listUrlPhotos!!) {
                imagesCollectionRealEstates(id).document().set(photoWithText)
            }

        }


    }

    private suspend fun addAllImagesAndGetUrls(listPhotos: List<PhotoWithText>): List<PhotoWithText> {


                val id = UUID.randomUUID().toString()


                val storageRef = storage.reference
                val imagesRef: StorageReference = storageRef.child("realEstates")

                for (photoWithText in listPhotos) {
                    val url = imagesRef.putFile(photoWithText.getPhotoUri()!!).await().storage.downloadUrl.await().toString()
                    photoWithText.setUrl(url)
                }
        return listPhotos
    }








    fun deleteRealEstate(id : String) {
        usersCollection.document(id).delete()
    }



}

