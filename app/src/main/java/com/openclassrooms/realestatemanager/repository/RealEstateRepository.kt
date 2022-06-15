package com.openclassrooms.realestatemanager.repository

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.MutableLiveData
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
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

    fun createRealEstate(type: String, price: Int, area: Int, numberRoom: Int, description: String, address: String,
        pointOfInterest: String, status: String, listPhotos: List<PhotoWithText> ?=null, dateEntry: String, dateSale: String,
    ) {

            val id = UUID.randomUUID().toString()

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
                firebaseAuth.currentUser?.displayName
            )
            usersCollection.document(id).set(realEstate)

        val storageRef = storage.reference

        //val realEstatesRef: StorageReference = storageRef.child("realEstates")

        //val realEstateRef: StorageReference = realEstatesRef.child("realEstates/$id")




        if(listPhotos!=null) {



            for (photoWithText in listPhotos) {

                val realEstateImage : StorageReference = storageRef.child("realEstates/$id/"+UUID.randomUUID().toString())

                val uploadTask = realEstateImage.putFile(photoWithText.getPhotoUri()!!)

                uploadTask.continueWithTask { task ->
                    if (!task.isSuccessful) {
                        task.exception?.let {
                            throw it
                        }
                    }
                    realEstateImage.downloadUrl
                }.addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val downloadUrl = task.result
                        photoWithText.setUrl(downloadUrl.toString())
                    }
                }

                imagesCollectionRealEstates(id).document().set(photoWithText)
            }
        }


    }
















    fun deleteRealEstate(id : String) {
        usersCollection.document(id).delete()
    }



}

