package com.openclassrooms.realestatemanager

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.PhotoWithTextFirebase
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

class FakeRealEstateDao : RealEstateDao {
    override suspend fun insertRealEstate(realEstate: RealEstateDatabase) {
        TODO("Not yet implemented")
    }

    private val flow = MutableSharedFlow<List<RealEstateDatabase>>()
    suspend fun emit(value: List<RealEstateDatabase>) = flow.emit(value)

    override fun realEstates(): Flow<List<RealEstateDatabase>> = flow

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