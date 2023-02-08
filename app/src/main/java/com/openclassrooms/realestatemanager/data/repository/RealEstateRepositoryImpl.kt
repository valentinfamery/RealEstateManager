package com.openclassrooms.realestatemanager.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.*
import com.openclassrooms.realestatemanager.domain.models.resultGeocoding.ResultGeocoding
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.service.ApiService
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.tasks.await
import org.joda.time.LocalDate
import org.joda.time.chrono.ISOChronology
import retrofit2.Call
import retrofit2.Callback
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
open class RealEstateRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val storageRef : StorageReference,
    private val context: Context,
    val realEstateDao: RealEstateDao): RealEstateRepository {


    override fun realEstates() : Flow<List<Estate>> = realEstateDao.realEstates()

    override suspend fun refreshRealEstatesFromFirestore() {

            val isNetWorkAvailable = Utils.isInternetAvailable()

            val firebaseAuth = FirebaseAuth.getInstance()

            if(isNetWorkAvailable && firebaseAuth.currentUser != null){
                Log.e("items", "repo1")

                val realEstates = firebaseFirestore.collection("real_estates").get().await().map {
                    it.toObject(Estate::class.java)
                }

                writeAndClearRoomDatabase(realEstates)
            }



    }

    suspend fun writeAndClearRoomDatabase(list : List<Estate>) {
        realEstateDao.clear()

        for (realEstate in list) {
            val photos = firebaseFirestore.collection("real_estates").document(realEstate.id).collection("listPhotoWithText").get().await().map {
                it.toObject(Photo::class.java)
            }
            val estate = Estate(
                realEstate.id,realEstate.type,realEstate.price,realEstate.area,realEstate.numberRoom,realEstate.description,
                realEstate.numberAndStreet,realEstate.numberApartment,realEstate.city,realEstate.region,realEstate.postalCode,
                realEstate.country,realEstate.status,realEstate.dateOfEntry,realEstate.dateOfSale,realEstate.realEstateAgent,
                realEstate.lat,realEstate.lng,realEstate.hospitalsNear,realEstate.schoolsNear,realEstate.shopsNear,realEstate.parksNear,
                photos
            )
            realEstateDao.insertRealEstate(estate)
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
        listPhotos: MutableList<Photo>,
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

            listPhotos.isNotEmpty()

            val address3 = "$numberAndStreet,$city,$region"
            val id = UUID.randomUUID().toString()
            val response = ApiService.`interface`.getResultGeocodingResponse(address3)






                            val lat = response.body()?.results?.get(0)?.geometry?.location?.lat
                            val lng = response.body()?.results?.get(0)?.geometry?.location?.lng
                            Log.e(lat.toString(), lng.toString())
                            Log.e("latLng", LatLng(lat!!, lng!!).toString())
                            val latLng = LatLng(lat, lng)
                            Log.e("result", latLng.toString())

                            val listPhotoFinal: MutableList<Photo> =
                                mutableListOf()

            for (photoWithText in listPhotos) {
                runBlocking {
                    launch {
                        val realEstateImage: StorageReference =
                            storageRef.child(
                                "realEstates/$id/${photoWithText.id}"
                            )
                        Log.e("uri", photoWithText.photoSource)
                        val urlFinal = withContext(Dispatchers.IO) {
                            realEstateImage.putFile(Uri.parse(photoWithText.photoSource))
                                .await().storage.downloadUrl.await()
                        }.toString()
                        Log.e("urlFinal", urlFinal)
                        photoWithText.photoSource = urlFinal
                        listPhotoFinal.add(photoWithText)
                    }
                }
            }

                            val realEstate = Estate(
                                id,
                                type,
                                price.toInt(),
                                area.toInt(),
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
                                listPhotoFinal,
                            )

                            firebaseFirestore.collection("real_estates").document(id)
                                .set(realEstate)

                            listPhotoFinal.forEach {
                                firebaseFirestore.collection("real_estates").document(id)
                                    .collection("listPhotoWithText").document(it.id).set(it)
                            }

                            runBlocking {
                                launch {

                                    realEstateDao.insertRealEstate(realEstate)
                                }
                            }


            Response.Success(true)


        } catch (e: Exception) {
            Response.Failure(e)
        }

    }

    override fun getPropertyBySearch(
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
    ): LiveData<List<Estate>> {

        val onTheMarketLessALastWeekInt = if(onTheMarketLessALastWeek) 1 else 0
        val soldOn3LastMonthInt = if(soldOn3LastMonth) 1 else 0
        val min3photosInt = if(min3photos) 1 else 0
        val schoolsInt = if(schools) 1 else 0
        val shopsInt = if(shops) 1 else 0


        val iso = ISOChronology.getInstance()
        val today = LocalDate(iso)

        Log.e("today",today.toString())

        val dateMinusThreeMonth = today.minusMonths(3)
        val dateMinus1Week = today.minusDays(7)

        Log.e("dateMinusThreeMonth",dateMinusThreeMonth.toString())
        Log.e("dateMinus1Week",dateMinus1Week.toString())

        val query = """SELECT * FROM Estate WHERE 
                        ('$type' ='' OR type LIKE '%$type%' ) AND 
                        ('$city' ='' OR city LIKE '%$city%' ) AND
                        ($schoolsInt = 0 OR schoolsNear = $schoolsInt ) AND 
                        ($shopsInt = 0 OR shopsNear = $shopsInt ) AND 
                        ($min3photosInt = 0 OR count_photo >= 3 ) AND
                        ($minSurface =0 AND $maxSurface = 0  OR  area BETWEEN $minSurface AND $maxSurface  ) AND 
                        ($minPrice =0 AND $maxPrice = 0  OR  price BETWEEN $minPrice AND $maxPrice ) AND 
                        ($onTheMarketLessALastWeekInt = 0  OR  dateOfEntry BETWEEN '$dateMinus1Week' AND '$today' ) AND 
                        ($soldOn3LastMonthInt = 0  OR dateOfSale BETWEEN '$dateMinusThreeMonth' AND '$today') """

        Log.e("query", query)


        return realEstateDao.getPropertyBySearch(SimpleSQLiteQuery(query))
    }

    override fun realEstateById(realEstateId: String): LiveData<Estate?> {
        Log.e("realEstateById()","repo")
        return realEstateDao.realEstateById(realEstateId)
    }

    override suspend fun updateRealEstate(
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
        listPhotoWithText: MutableList<Photo>,
        itemRealEstate: Estate
    ): Response<Boolean> {
        return try {
            Response.Loading

            listPhotoWithText.isNotEmpty()


            val rEcollection = firebaseFirestore.collection("real_estates")

            if(entryType != itemRealEstate.type ){
                rEcollection.document(id).update("type",entryType)
            }

            if(entryPrice.toInt() != itemRealEstate.price){
                rEcollection.document(id).update("price",entryPrice.toInt())
            }

            if(entryArea.toInt() != itemRealEstate.area){
                rEcollection.document(id).update("area",entryArea.toInt())
            }

            Log.e(itemRealEstate.numberRoom,entryNumberRoom)

            if(entryNumberRoom != itemRealEstate.numberRoom){
                rEcollection.document(id).update("numberRoom",entryNumberRoom)
            }

            Log.e(itemRealEstate.description,entryDescription)

            if(entryDescription != itemRealEstate.description){
                rEcollection.document(id).update("description",entryDescription)
            }

            if(checkedStateHopital.value !=itemRealEstate.hospitalsNear){
                rEcollection.document(id).update("hospitalsNear",checkedStateHopital.value)
            }
            if(checkedStateSchool.value != itemRealEstate.schoolsNear){
                rEcollection.document(id).update("schoolsNear",checkedStateSchool.value)
            }
            if(checkedStateShops.value != itemRealEstate.shopsNear){
                rEcollection.document(id).update("shopsNear",checkedStateShops.value)
            }
            if(checkedStateParks.value != itemRealEstate.parksNear){
                rEcollection.document(id).update("parksNear",checkedStateParks.value)
            }

            if(entryStatus != itemRealEstate.status){
                rEcollection.document(id).update("status",entryStatus)
                rEcollection.document(id).update("dateOfSale",textDateOfSale)
            }

            if(entryNumberApartement != itemRealEstate.numberApartment){
                rEcollection.document(id).update("numberApartment",entryNumberApartement)
            }


            if(entryNumberAndStreet != itemRealEstate.numberAndStreet || entryCity != itemRealEstate.city ||
               entryRegion != itemRealEstate.region || entryPostalCode != itemRealEstate.postalCode || entryCountry != itemRealEstate.country   ){

                val address3 = "$entryNumberAndStreet,$entryCity,$entryRegion"
                val response = ApiService.`interface`.getResultGeocodingResponse(address3)


                        val lat = response.body()?.results?.get(0)?.geometry?.location?.lat
                        val lng = response.body()?.results?.get(0)?.geometry?.location?.lng

                        rEcollection.document(id).update("lat",lat)
                        rEcollection.document(id).update("lng",lng)



                rEcollection.document(id).update("numberAndStreet",entryNumberAndStreet)
                rEcollection.document(id).update("city",entryCity)
                rEcollection.document(id).update("region",entryRegion)
                rEcollection.document(id).update("postalCode",entryPostalCode)
                rEcollection.document(id).update("country",entryCountry)

            }



            for(photoWithText in listPhotoWithText){
                Log.e("photoWithTextToAddLater",photoWithText.toString())

                val photo = firebaseFirestore.collection("real_estates").document(id).collection("listPhotoWithText").document(photoWithText.id).get().await().toObject(Photo::class.java)

                delay(1000)
                val realEstateImage: StorageReference = storageRef.child(
                    "realEstates/$id/${photoWithText.id}"
                )


                if(photo == null) {
                        if(!photoWithText.toDeleteLatter) {



                            val urlFinal = withContext(Dispatchers.IO) {
                                realEstateImage.putFile(Uri.parse(photoWithText.photoSource))
                                    .await().storage.downloadUrl.await()
                            }.toString()
                            Log.e("urlFinal", urlFinal)
                            photoWithText.photoSource = urlFinal

                            firebaseFirestore.collection("real_estates").document(id)
                                .collection("listPhotoWithText").document(photoWithText.id)
                                .set(photoWithText)

                        }
                }


                    if(photo != null) {
                        if(photoWithText.toDeleteLatter){
                                realEstateImage.delete().await()
                                firebaseFirestore.collection("real_estates").document(id)
                                    .collection("listPhotoWithText").document(photoWithText.id).delete()
                        }else {


                            if (photo.photoSource != photoWithText.photoSource) {


                                realEstateImage.delete().await()

                                delay(1000)

                                val urlFinal = withContext(Dispatchers.IO) {
                                    realEstateImage.putFile(Uri.parse(photoWithText.photoSource))
                                        .await().storage.downloadUrl.await()
                                }.toString()
                                Log.e("urlFinal", urlFinal)
                                photoWithText.photoSource = urlFinal

                                firebaseFirestore.collection("real_estates").document(id)
                                    .collection("listPhotoWithText").document(photoWithText.id)
                                    .update("photoSource", photoWithText.photoSource)
                            }
                            if (photo.text != photoWithText.text) {
                                firebaseFirestore.collection("real_estates").document(id)
                                    .collection("listPhotoWithText").document(photoWithText.id)
                                    .update("text", photoWithText.text)
                            }

                        }
                    }




            }


            val realEstate = rEcollection.document(id).get().await().toObject(Estate::class.java)
            runBlocking {
                launch {
                    val photos = firebaseFirestore.collection("real_estates").document(realEstate!!.id).collection("listPhotoWithText").get().await().map {
                        it.toObject(Photo::class.java)
                    }
                    val estate1 = Estate(
                        realEstate.id,realEstate.type,realEstate.price,realEstate.area,realEstate.numberRoom,realEstate.description,
                        realEstate.numberAndStreet,realEstate.numberApartment,realEstate.city,realEstate.region,realEstate.postalCode,
                        realEstate.country,realEstate.status,realEstate.dateOfEntry,realEstate.dateOfSale,realEstate.realEstateAgent,
                        realEstate.lat,realEstate.lng,realEstate.hospitalsNear,realEstate.schoolsNear,realEstate.shopsNear,realEstate.parksNear,
                        photos
                    )

                    realEstateDao.updateEstate(estate1)
                }
            }

            Response.Success(true)
        }catch (e: Exception) {
            Log.e("repoUpdateFailure",e.message.toString())
            Response.Failure(e)
        }
    }


}