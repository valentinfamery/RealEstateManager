package com.openclassrooms.realestatemanager

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.domain.repository.RealEstateRepository
import com.openclassrooms.realestatemanager.domain.use_case.UseCases
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import junit.framework.Assert
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class RealEstateRepositoryInstrumentedTest {

    @Mock
    lateinit var realEstateRepository : RealEstateRepository

    @Mock
    lateinit var useCases: UseCases

    lateinit var realEstateViewModel: RealEstateViewModel

    @Before
    fun setUp(){
        MockitoAnnotations.openMocks(this)
        realEstateViewModel = RealEstateViewModel(useCases,realEstateRepository)
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
        `when`(realEstateRepository.realEstates()).thenReturn(flowOf(expectedRealEstates))

        val result = realEstateViewModel.realEstates.first()
        assertEquals(expectedRealEstates, result)
        println(result.size.toString())
        // Assert



    }

}