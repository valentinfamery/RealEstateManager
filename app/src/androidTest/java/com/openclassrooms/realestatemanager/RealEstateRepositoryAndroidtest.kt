package com.openclassrooms.realestatemanager

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.data.repository.RealEstateRepositoryImpl
import com.openclassrooms.realestatemanager.database.RealEstateRoomDatabase
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.*
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RealEstateRepositoryAndroidtest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val dispatcher = UnconfinedTestDispatcher()
    private val scope = TestScope(dispatcher)


    lateinit var instrumentationContext: Context

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
        instrumentationContext = ApplicationProvider.getApplicationContext();
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

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()


        val dao = db.realEstateDao()
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

        dao.insertRealEstate(realEstate1)
        dao.insertRealEstate(realEstate2)

        viewModel.realEstates.test {
            val list = awaitItem()
            assert(list.contains(realEstate1))
            assert(list.contains(realEstate2))
        }








    }

    @Test
    fun refreshRealEstatesFromFirestoreTest() = scope.runTest {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

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

        repository.writeAndClearRoomDatabase(expectedRealEstates)

        viewModel.realEstates.test {
            delay(250)
            val list = expectMostRecentItem()
            assert(list == expectedRealEstates)
        }

    }

    @Test
    fun testAddListPhotoNewScreenState() {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1")
        val list = listOf(photoWithTextFirebase)
        viewModel._listPhotoNewScreenState.value = list
        viewModel.addListPhotoNewScreenState(photoWithTextFirebase)
        assertEquals(list + photoWithTextFirebase, viewModel.listPhotoNewScreenState.value)
    }

    @Test
    fun testDeleteListPhotoNewScreenState() {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1")
        val list = listOf(photoWithTextFirebase)
        viewModel._listPhotoNewScreenState.value = list
        viewModel.deleteListPhotoNewScreenState(photoWithTextFirebase)
        assertEquals(emptyList<PhotoWithTextFirebase>(), viewModel.listPhotoNewScreenState.value)
    }

    @Test
    fun testUpdatePhotoSourceElementNewScreen() {
        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)
        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1")
        val list = listOf(photoWithTextFirebase)
        viewModel._listPhotoNewScreenState.value = list
        viewModel.updatePhotoSourceElementNewScreen("1", "photo2.jpg")
        assertEquals("photo2.jpg", viewModel.listPhotoNewScreenState.value[0].photoSource)
    }

    @Test
    fun testUpdatePhotoTextElementNewScreen() {
        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)
        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1")
        val list = listOf(photoWithTextFirebase)
        viewModel._listPhotoNewScreenState.value = list
        viewModel.updatePhotoTextElementNewScreen("1", "text2")
        assertEquals("text2", viewModel.listPhotoNewScreenState.value[0].text)
    }

    @Test
    fun testFillMyUiState() {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1")
        val list = listOf(photoWithTextFirebase)
        viewModel.fillMyUiState(list)
        assertEquals(list, viewModel.listPhotoEditScreenState.value)
    }

    @Test
    fun testAddPhoto() {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1")
        val list = listOf(photoWithTextFirebase)
        viewModel._listPhotoEditScreenState.value = list
        viewModel.addPhoto(photoWithTextFirebase)
        assertEquals(list + photoWithTextFirebase, viewModel.listPhotoEditScreenState.value)
    }

    @Test
    fun testUpdatePhotoWithTextInListEditScreenToDeleteLatterToTrue() {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1")
        val list = listOf(photoWithTextFirebase)
        viewModel._listPhotoEditScreenState.value = list
        viewModel.updatePhotoWithTextInListEditScreenToDeleteLatterToTrue("1")
        assertEquals(true, viewModel.listPhotoEditScreenState.value[0].toDeleteLatter)
    }

    @Test
    fun testUpdateAttributeToUpdate() {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1",toUpdateLatter = false)
        val list = listOf(photoWithTextFirebase)
        viewModel._listPhotoEditScreenState.value = list
        viewModel.updateAttributeToUpdate("1")
        assertEquals(true, viewModel.listPhotoEditScreenState.value[0].toUpdateLatter)
    }

    @Test
    fun testUpdateAttributePhotoSource() {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1")
        val list = listOf(photoWithTextFirebase)
        viewModel._listPhotoEditScreenState.value = list
        viewModel.updateAttributePhotoSource("1","photo2.jpg")
        assertEquals("photo2.jpg", viewModel.listPhotoEditScreenState.value[0].photoSource)
    }

    @Test
    fun testUpdateAttributePhotoText() {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        val photoWithTextFirebase = PhotoWithTextFirebase( "photo1.jpg", "text1","1")
        val list = listOf(photoWithTextFirebase)
        viewModel._listPhotoEditScreenState.value = list
        viewModel.updateAttributePhotoText("1","text2")
        assertEquals("text2", viewModel.listPhotoEditScreenState.value[0].text)
    }


}

