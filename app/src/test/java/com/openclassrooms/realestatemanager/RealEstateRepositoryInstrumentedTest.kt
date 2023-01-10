package com.openclassrooms.realestatemanager


import android.content.Context
import android.database.Cursor

import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import app.cash.turbine.test
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.data.repository.RealEstateRepositoryImpl
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository

import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockito.Mockito.mock

import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RealEstateRepositoryInstrumentedTest {

    private val scope = TestScope()

    private val realEstateRepository = mock(RealEstateRepository::class.java)

    @BeforeEach
    fun setUp(){
        Dispatchers.setMain(UnconfinedTestDispatcher(scope.testScheduler))
        MockitoAnnotations.openMocks(this)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun realEstates_returnsCorrectData() = runTest{
        // Arrange

        val realEstateViewModel = RealEstateViewModel(realEstateRepository)

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






        realEstateViewModel.realEstates.test {

            val result = awaitItem()

            for(real in result) {
                println(real.id)
            }

            println("Good1")

            assertEquals(listOf<List<RealEstateDatabase>>(), result)

            println("Good2")


        }

    }



}
