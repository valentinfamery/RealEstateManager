package com.openclassrooms.realestatemanager


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.MutableState
import androidx.lifecycle.LiveData
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
class RealEstateRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()
    private val scope = TestScope(dispatcher)

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }



    @Test
    fun realEstates_returnsCorrectData() = scope.runTest{
        // Arrange
        val repository = FakeRepo()

        val realEstateViewModel = RealEstateViewModel(repository)

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



            repository.emit(expectedRealEstates)


            val result = realEstateViewModel.realEstates.value

            for(real in result) {
                println(real.id)
            }

            println("Good1")

            assertEquals(expectedRealEstates, result)

            println("Good2")




    }



}

class FakeRepo : RealEstateRepository {
    override suspend fun refreshRealEstatesFromFirestore() {
        TODO("Not yet implemented")
    }

    private val flow = MutableSharedFlow<List<RealEstateDatabase>>()
    suspend fun emit(value: List<RealEstateDatabase>) = flow.emit(value)

    override fun realEstates(): Flow<List<RealEstateDatabase>> = flow

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
        listPhotos: MutableList<PhotoWithTextFirebase>?,
        dateEntry: String,
        dateSale: String,
        realEstateAgent: String,
        checkedStateHospital: Boolean,
        checkedStateSchool: Boolean,
        checkedStateShops: Boolean,
        checkedStateParks: Boolean
    ): Response<Boolean> {
        TODO("Not yet implemented")
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
        TODO("Not yet implemented")
    }

    override fun realEstateById(realEstateId: String): LiveData<RealEstateDatabase?> {
        TODO("Not yet implemented")
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
        listPhotoWithText: MutableList<PhotoWithTextFirebase>,
        itemRealEstate: RealEstateDatabase
    ): Response<Boolean> {
        TODO("Not yet implemented")
    }

}

