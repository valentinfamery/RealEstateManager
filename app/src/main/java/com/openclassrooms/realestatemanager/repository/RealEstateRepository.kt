package com.openclassrooms.realestatemanager.repository

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.UploadTask
import com.openclassrooms.realestatemanager.models.RealEstate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class RealEstateRepository {
    private var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    private val usersCollection: CollectionReference
        get() = FirebaseFirestore.getInstance().collection(
            COLLECTION_REAL_ESTATE
        )

    companion object {
        private const val COLLECTION_REAL_ESTATE = "real_estates"
    }

    val getRealEstates: MutableLiveData<List<RealEstate>>
        get() {
            Log.e("getRealEstates","getRealEstates")
            val result = MutableLiveData<List<RealEstate>>()
            usersCollection.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                val userList = queryDocumentSnapshots.toObjects(
                    RealEstate::class.java
                )
                result.postValue(userList)
            }
            return result
        }

    suspend fun createRealEstate(
        type: String,
        price: Int,
        area: Int,
        numberRoom: Int,
        description: String,
        address: String,
        pointOfInterest: String,
        status: String,
        listPhotosUri: List<Uri>,
        dateEntry: String,
        dateSale: String, ) {

        return withContext(Dispatchers.IO) {
            val id = UUID.randomUUID().toString()
            val listUrlPhotos = mutableListOf<String>()

            val storageRef = storage.reference
            val imagesRef: StorageReference = storageRef.child("realEstates")

            for (uri in listPhotosUri) {


                val url = imagesRef.putFile(uri).await().storage.downloadUrl.await().toString()
                listUrlPhotos.add(url)
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

        }
    }








    fun deleteRealEstate(id : String) {
        usersCollection.document(id).delete()
    }



}

