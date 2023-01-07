package com.openclassrooms.realestatemanager


import android.content.Context
import android.database.Cursor
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import app.cash.turbine.test
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.data.repository.RealEstateRepositoryImpl
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock

import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class RealEstateRepositoryInstrumentedTest {

    val firebaseFirestore = mock(FirebaseFirestore::class.java)
    val storageRef = mock(StorageReference::class.java)
    val context = mock(Context::class.java)

    var realEstateDao = FakeRealEstateDao()
    private val realEstateRepository = RealEstateRepositoryImpl(firebaseFirestore,storageRef,context,realEstateDao)

    lateinit var realEstateViewModel: RealEstateViewModel

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        realEstateViewModel = RealEstateViewModel(realEstateRepository)
    }

    @Test
    fun realEstates_returnsCorrectData() = runTest{
        // Arrange
        val realEstate1 = RealEstateDatabase(
            id = "1",
            type = "Maison",
            price = 200000,
            area = 3,
            numberRoom = "2",
            lat = 44.5,
            lng = -0.5,
            hospitalsNear = true,
            schoolsNear = false,
            shopsNear = true,
            parksNear = false,
            listPhotoWithText = listOf(
                PhotoWithTextFirebase(
                    photoSource = "photo1.jpg",
                    text = "Description de la photo 1",
                    id = "photo1"
                ),
                PhotoWithTextFirebase(
                    photoSource = "photo2.jpg",
                    text = "Description de la photo 2",
                    id = "photo2"
                )
            )
        )
        val realEstate2 = RealEstateDatabase(
            id = "2",
            type = "Appartement",
            price = 100000,
            area = 2,
            numberRoom = "1",
            lat = 45.5,
            lng = -1.5,
            hospitalsNear = false,
            schoolsNear = true,
            shopsNear = false,
            parksNear = true,
            listPhotoWithText = listOf(
                PhotoWithTextFirebase(
                    photoSource = "photo3.jpg",
                    text = "Description de la photo 3",
                    id = "photo3"
                ),
                PhotoWithTextFirebase(
                    photoSource = "photo4.jpg",
                    text = "Description de la photo 4",
                    id = "photo4"
                )
            )
        )

        val expectedRealEstates = listOf(realEstate1, realEstate2)






        realEstateRepository.realEstates().test {
            realEstateDao.emit(expectedRealEstates)

            val result = awaitItem()

            for(real in result) {
                println(real.id)
            }

            println("Good1")

            assertEquals(expectedRealEstates, result)

            println("Good2")


        }

    }



}






class FakeRealEstateDao() : RealEstateDao {
    override suspend fun insertRealEstate(realEstate: RealEstateDatabase) {
        TODO("Not yet implemented")
    }

    private val flow = MutableSharedFlow<List<RealEstateDatabase>>()
    suspend fun emit(value: List<RealEstateDatabase>) {
        flow.emit(value)
        println("emitDao()")
    }
    override fun realEstates(): Flow<List<RealEstateDatabase>>  = flow

    override suspend fun clear() {
        TODO("Not yet implemented")
    }

    override fun realEstateById(realEstateId: String): LiveData<RealEstateDatabase?> {
        TODO("Not yet implemented")
    }

    override fun getRealEstatesWithCursor(): Cursor {
        TODO("Not yet implemented")
    }

    override suspend fun updateRealEstate(
        entryType: String,
        id: String,
        entryPrice: Int,
        entryArea: Int,
        entryNumberRoom: String,
        entryDescription: String,
        hospitalsNear: Boolean,
        schoolsNear: Boolean,
        shopsNear: Boolean,
        parksNear: Boolean,
        entryStatus: String,
        textDateOfSale: String,
        entryNumberApartement: String,
        entryNumberAndStreet: String,
        entryCity: String,
        entryRegion: String,
        entryPostalCode: String,
        entryCountry: String,
        listPhotoWithText: List<PhotoWithTextFirebase>?
    ) {
        TODO("Not yet implemented")
    }

    override suspend fun updateRealEstateLatLng(id: String, lat: Double?, lng: Double?) {
        TODO("Not yet implemented")
    }

    override fun getPropertyBySearch(supportSQLiteQuery: SupportSQLiteQuery): LiveData<List<RealEstateDatabase>> {
        TODO("Not yet implemented")
    }

}