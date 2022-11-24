package com.openclassrooms.realestatemanager.data.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SimpleSQLiteQuery
import androidx.sqlite.db.SupportSQLiteQuery
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
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
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
        return realEstateDao.realEstates()
    }

    override suspend fun refreshRealEstatesFromFirestore() {

            val isNetWorkAvailable = Utils.isInternetAvailable(context)

        val firebaseAuth = FirebaseAuth.getInstance()

            if(isNetWorkAvailable && firebaseAuth.currentUser != null){
                Log.e("items", "repo1")


                val realEstates = firebaseFirestore.collection("real_estates").get().await().map {
                    it.toObject(RealEstate::class.java)
                }

                realEstateDao.clear()

                for (realEstate in realEstates) {

                    val realEstateDatabase = RealEstateDatabase(
                        realEstate.id!!,
                        realEstate.type!!,
                        realEstate.price!!.toInt(),
                        realEstate.area!!.toInt(),
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
                        realEstate.listPhotoWithText?.size
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
                            listPhotoWithTextFirebaseFinal
                        )
                        firebaseFirestore.collection("real_estates").document(id).set(realEstate)

                        val realEstateDatabase  = RealEstateDatabase(
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
                        listPhotoWithTextFirebaseFinal,
                        )


                        runBlocking {
                            launch {
                                realEstateDao.insertRealEstate(realEstateDatabase)
                            }
                        }

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
    ): LiveData<List<RealEstateDatabase>> {
        val c = Calendar.getInstance()
        val fmt: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

        val dateToday = DateTime(c.time)
        val dateTodayFinal = dateToday.toString(fmt)

        val dateMinusThreeMonth = dateToday.minusMonths(3).toString(fmt)
        val dateMinus1Week = dateToday.minusDays(7).toString(fmt)

        val query = """SELECT * FROM RealEstateDatabase WHERE 
                        ('$type' ='' OR type LIKE '%$type%' ) AND 
                        ('$city' ='' OR city LIKE '%$city%' ) AND
                        ($schools = false OR schoolsNear = $schools ) AND 
                        ($shops = false OR shopsNear = $shops ) AND 
                        ($min3photos = false OR count_photo >= 3 ) AND
                        ($minSurface =0 AND $maxSurface = 0  OR  area BETWEEN $minSurface AND $maxSurface  ) AND 
                        ($minPrice =0 AND $maxPrice = 0  OR  price BETWEEN $minPrice AND $maxPrice ) AND 
                        ($onTheMarketLessALastWeek = false  OR  dateOfEntry BETWEEN '$dateMinus1Week' AND '$dateTodayFinal' ) AND 
                        ($soldOn3LastMonth = false  OR  dateOfSale <> '00/00/0000' OR  dateOfSale BETWEEN '$dateMinusThreeMonth' AND '$dateTodayFinal' ) """




        return realEstateDao.getPropertyBySearch(SimpleSQLiteQuery(query))
    }

    override fun realEstateById(realEstateId: String): LiveData<RealEstateDatabase?> {
        Log.e("realEstateById()","repo")
        return realEstateDao.realEstateById(realEstateId)
    }


}