package com.openclassrooms.realestatemanager

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.data.repository.RealEstateRepositoryImpl
import com.openclassrooms.realestatemanager.database.RealEstateRoomDatabase
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import junit.framework.Assert
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.*

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RealEstateRepositoryAndroidtest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()
    private val scope = TestScope(dispatcher)
    private lateinit var db: RealEstateRoomDatabase

    lateinit var instrumentationContext: Context

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        instrumentationContext = ApplicationProvider.getApplicationContext();

        db = Room.inMemoryDatabaseBuilder(
            instrumentationContext, RealEstateRoomDatabase::class.java).build()

    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun realEstates_returnsCorrectData() = scope.runTest{

        val firebaseFirestore = mock(FirebaseFirestore::class.java)
        val storageReference = mock(StorageReference::class.java)
        // Arrange
        val dao = FakeRealEstateDao()
        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

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


        dao.emit(expectedRealEstates)



        val result = viewModel.realEstates.value

        for(real in result) {
            println(real.id)
        }

        println("Good1")

        Assert.assertEquals(expectedRealEstates, result)

        println("Good2")




    }

    @Test
    fun refreshRealEstatesFromFirestoreTest() = scope.runTest {
        val firebaseFirestore = mock(FirebaseFirestore::class.java)
        val storageReference = mock(StorageReference::class.java)
        // Arrange
        val dao = FakeRealEstateDao()
        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        viewModel.refreshRealEstates()

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

        //`when`(repository.getRealEstatesFromFirestore()).thenReturn(expectedRealEstates)
        verify(dao).clear()
        verify(dao).insertRealEstate(realEstate1)
    }



}

