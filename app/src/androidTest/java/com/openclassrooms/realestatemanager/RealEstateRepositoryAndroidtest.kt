package com.openclassrooms.realestatemanager

import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import app.cash.turbine.test
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.StorageReference
import com.openclassrooms.realestatemanager.data.repository.RealEstateRepositoryImpl
import com.openclassrooms.realestatemanager.database.RealEstateRoomDatabase
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import junit.framework.Assert.assertEquals
import junit.framework.Assert.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.*
import org.joda.time.LocalDate
import org.joda.time.chrono.ISOChronology
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
    fun insertRealEstate() = scope.runTest{

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

        dao.insertRealEstate(realEstate1)

        viewModel.realEstates.test {
            val list = awaitItem()
            assert(list.contains(realEstate1))
        }
    }

    @Test
    fun clearRoomRealEstateDatabase() = scope.runTest {

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

        dao.insertRealEstate(realEstate1)


        dao.clear()

        viewModel.realEstates.test {
            delay(250)
            val list = expectMostRecentItem()
            assert(list == listOf<RealEstateDatabase>())
        }

    }

    @Test
    fun realEstateById() = scope.runTest{
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

        dao.insertRealEstate(realEstate1)
        dao.insertRealEstate(realEstate2)

        viewModel.realEstateById("2").observeForever {
            if (it != null) {
                assert(it.equals(realEstate2))
            } else {
                assert(false)
            }
        }


    }

    @Test
    fun getPropertyBySearch() = scope.runTest{

        val iso = ISOChronology.getInstance()
        val today = LocalDate(iso)

        val dateMinusThreeMonth = today.minusMonths(3)
        val dateMinus1Week = today.minusDays(7)

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)


        val listing1 = RealEstateDatabase(
            id = "123",
            type = "House",
            price = 500000,
            area = 1000,
            numberRoom = "5",
            description = "Beautiful 5 bedroom house for sale in a great neighborhood.",
            numberAndStreet = "1234 Main St",
            numberApartment = "",
            city = "New York",
            region = "NY",
            postalCode = "10001",
            country = "USA",
            status = "For Sale",
            dateOfEntry = "2022-01-01",
            dateOfSale = "",
            realEstateAgent = "John Smith",
            lat = 40.730610,
            lng = -73.935242,
            hospitalsNear = true,
            schoolsNear = true,
            shopsNear = true,
            parksNear = true,
            listPhotoWithText = listOf(PhotoWithTextFirebase("https://example.com/image1.jpg","This is a photo of the living room","1"),
                PhotoWithTextFirebase("https://example.com/image2.jpg","This is a photo of the kitchen","2")),
            count_photo = 2
        )

        val listing2 = RealEstateDatabase(
            id = "456",
            type = "Apartment",
            price = 200000,
            area = 500,
            numberRoom = "3",
            description = "Beautiful 3 bedroom apartment for sale in a great location.",
            numberAndStreet = "5678 Park Ave",
            numberApartment = "12",
            city = "Los Angeles",
            region = "CA",
            postalCode = "90001",
            country = "USA",
            status = "For Sale",
            dateOfEntry = dateMinus1Week.toString(),
            dateOfSale = dateMinusThreeMonth.toString(),
            realEstateAgent = "Jane Smith",
            lat = 34.052235,
            lng = -118.243683,
            hospitalsNear = true,
            schoolsNear = true,
            shopsNear = true,
            parksNear = true,
            listPhotoWithText = listOf(
                PhotoWithTextFirebase("https://example.com/image3.jpg","This is a photo of the living room","3"),
                PhotoWithTextFirebase("https://example.com/image4.jpg","This is a photo of the kitchen","4"),
                PhotoWithTextFirebase("https://example.com/image4.jpg","This is a photo of the kitchen","4")
            ),
            count_photo = 3
        )

        dao.insertRealEstate(listing1)
        dao.insertRealEstate(listing2)

        viewModel.getPropertyBySearch(
            listing2.type.toString(),listing2.city.toString(),250,750,
            100000,300000,true,
            true,true,listing2.schoolsNear,listing2.shopsNear).observeForever {
            if (it != null) {
                assert(it.contains(listing2))
            } else {
                assert(false)
            }
        }


    }

    @Test
    fun updateRealEstate() = scope.runTest{
        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)


        val listing1 = RealEstateDatabase(
            id = "123",
            type = "House",
            price = 500000,
            area = 1000,
            numberRoom = "5",
            description = "Beautiful 5 bedroom house for sale in a great neighborhood.",
            numberAndStreet = "1234 Main St",
            numberApartment = "",
            city = "New York",
            region = "NY",
            postalCode = "10001",
            country = "USA",
            status = "For Sale",
            dateOfEntry = "2022-01-01",
            dateOfSale = "",
            realEstateAgent = "John Smith",
            lat = 40.730610,
            lng = -73.935242,
            hospitalsNear = true,
            schoolsNear = true,
            shopsNear = true,
            parksNear = true,
            listPhotoWithText = listOf(PhotoWithTextFirebase("https://example.com/image1.jpg","This is a photo of the living room","1"),
                PhotoWithTextFirebase("https://example.com/image2.jpg","This is a photo of the kitchen","2")),
            count_photo = 2
        )

        val listing2 = RealEstateDatabase(
            id = "456",
            type = "Apartment",
            price = 200000,
            area = 500,
            numberRoom = "3",
            description = "Beautiful 3 bedroom apartment for sale in a great location.",
            numberAndStreet = "5678 Park Ave",
            numberApartment = "12",
            city = "Los Angeles",
            region = "CA",
            postalCode = "90001",
            country = "USA",
            status = "For Sale",
            dateOfEntry = "2022-01-01",
            dateOfSale = "",
            realEstateAgent = "Jane Smith",
            lat = 34.052235,
            lng = -118.243683,
            hospitalsNear = true,
            schoolsNear = true,
            shopsNear = true,
            parksNear = true,
            listPhotoWithText = listOf(PhotoWithTextFirebase("https://example.com/image3.jpg","This is a photo of the living room","3"),
                PhotoWithTextFirebase("https://example.com/image4.jpg","This is a photo of the kitchen","4")),
            count_photo = 2
        )

        dao.insertRealEstate(listing1)

        dao.updateRealEstate(
            listing2.type!!, listing1.id, listing2.price!!,
            listing2.area!!,listing2.numberRoom!!,listing2.description!!,
            listing2.hospitalsNear,listing2.schoolsNear,listing2.shopsNear,
            listing2.parksNear, listing2.status!!,listing2.dateOfSale!!,
            listing2.numberApartment!!,
            listing2.numberAndStreet!!, listing2.city!!,
            listing2.region!!,
            listing2.postalCode!!, listing2.country!!,listing2.listPhotoWithText)
        dao.updateRealEstateLatLng(listing1.id,listing2.lat,listing2.lng)



        viewModel.realEstates.test {
            delay(250)
            val list = awaitItem()
            assert(list[0].type.equals("Apartment"))
            assert(list[0].price == 200000)
            assert(list[0].area == 500)
            assert(list[0].numberRoom == "3")
            assert(list[0].description == "Beautiful 3 bedroom apartment for sale in a great location.")
            assert(list[0].numberAndStreet == "5678 Park Ave")
            assert(list[0].numberApartment == "12")
            assert(list[0].city == "Los Angeles")
            assert(list[0].region == "CA")
            assert(list[0].postalCode == "90001")
            assert(list[0].country == "USA")
            assert(list[0].status == "For Sale")
            assert(list[0].dateOfEntry == "2022-01-01")
            assert(list[0].dateOfSale == "")
            assert(list[0].lat == 34.052235)
            assert(list[0].lng == -118.243683)
            assert(list[0].hospitalsNear == true)
            assert(list[0].schoolsNear == true)
            assert(list[0].shopsNear == true)
            assert(list[0].parksNear == true)
            assert(list[0].listPhotoWithText == listOf(PhotoWithTextFirebase("https://example.com/image3.jpg","This is a photo of the living room","3"),
                PhotoWithTextFirebase("https://example.com/image4.jpg","This is a photo of the kitchen","4")))

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

    @Test
    fun testCreateRealEstateResponse() = runTest {
        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)
        viewModel.createRealEstateResponse = Response.Success(true)
        assertTrue((viewModel.createRealEstateResponse as Response.Success<Boolean>).data)

        viewModel.createRealEstateResponse = Response.Failure(Exception("Error message"))
        assertTrue((viewModel.createRealEstateResponse as Response.Failure).e.message == "Error message")
    }

    @Test
    fun testUpdateRealEstateResponse() = runTest {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        viewModel.updateRealEstateResponse = Response.Success(true)
        assertTrue((viewModel.updateRealEstateResponse as Response.Success<Boolean>).data == true)

        viewModel.updateRealEstateResponse = Response.Failure(Exception("Error message"))
        assertTrue((viewModel.updateRealEstateResponse as Response.Failure).e.message == "Error message")
    }

    @Test
    fun testSelectedItem() = runTest {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        viewModel.selectedItem.value = 1
        assertTrue(viewModel.selectedItem.value == 1)

        viewModel.selectedItem.value = 2
        assertTrue(viewModel.selectedItem.value == 2)
    }

    @Test
    fun testList() = runTest {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)


        viewModel.list = Response.Success(true)
        assertTrue((viewModel.list as Response.Success<Boolean>).data == true)

        viewModel.list = Response.Failure(Exception("Error message"))
        assertTrue((viewModel.list as Response.Failure).e.message == "Error message")
    }

    @Test
    fun testIsRefreshing() = runTest {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        viewModel._isRefreshing.value = true
        assertTrue(viewModel.isRefreshing.value == true)

        viewModel._isRefreshing.value = false
        assertTrue(viewModel.isRefreshing.value == false)
    }

    @Test
    fun testRealEstateIdDetail() = runTest {

        val firebaseFirestore = mock(FirebaseFirestore::class.java)

        val storageReference = mock(StorageReference::class.java)

        val db = Room.inMemoryDatabaseBuilder(instrumentationContext, RealEstateRoomDatabase::class.java).build()

        val dao = db.realEstateDao()

        val repository = RealEstateRepositoryImpl(firebaseFirestore,storageReference,instrumentationContext,dao)

        val viewModel = RealEstateViewModel(repository)

        viewModel.realEstateIdDetail.value = "123"
        assertTrue(viewModel.realEstateIdDetail.value == "123")

        viewModel.realEstateIdDetail.value = "456"
        assertTrue(viewModel.realEstateIdDetail.value == "456")
    }


}

