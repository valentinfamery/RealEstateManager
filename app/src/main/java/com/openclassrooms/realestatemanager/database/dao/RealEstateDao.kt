package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRealEstate(realEstate: RealEstateDatabase)

    @Query("SELECT * FROM RealEstateDatabase")
    fun realEstates(): Flow<List<RealEstateDatabase>>

    @Query("DELETE FROM RealEstateDatabase")
    suspend fun clear()



    @Query("SELECT * FROM RealEstateDatabase WHERE id = :realEstateId")
    fun realEstateById(realEstateId : String):LiveData<RealEstateDatabase?>

    @Query("SELECT * FROM RealEstateDatabase")
    fun getRealEstatesWithCursor(): Cursor

    @Query("UPDATE  RealEstateDatabase SET " +
            "type = (CASE WHEN type NOT LIKE :entryType THEN (:entryType) ELSE type END)," +
            "price = (CASE WHEN price NOT LIKE :entryPrice THEN (:entryPrice) ELSE price END)," +
            "area = (CASE WHEN area NOT LIKE :entryArea THEN (:entryArea) ELSE area END)," +
            "numberRoom =(CASE WHEN numberRoom NOT LIKE :entryNumberRoom THEN(:entryNumberRoom)ELSE numberRoom END)," +
            "description =(CASE WHEN description NOT LIKE :entryDescription THEN(:entryDescription)ELSE description END)," +
            "hospitalsNear =(CASE WHEN hospitalsNear NOT LIKE :hospitalsNear THEN(:hospitalsNear)ELSE hospitalsNear END)," +
            "schoolsNear = (CASE WHEN schoolsNear NOT LIKE :schoolsNear THEN (:schoolsNear)ELSE schoolsNear END)," +
            "shopsNear = (CASE WHEN shopsNear NOT LIKE :shopsNear THEN (:shopsNear)ELSE shopsNear END)," +
            "parksNear = (CASE WHEN parksNear NOT LIKE :parksNear THEn (:parksNear)ELSE parksNear END)," +
            "status = (CASE WHEN status NOT LIKE :entryStatus THEN (:entryStatus)ELSE status END)," +
            "dateOfSale = (CASE WHEN dateOfSale NOT LIKE :textDateOfSale THEN (:textDateOfSale)ELSE dateOfSale END)," +
            "numberApartment = (CASE WHEN numberApartment NOT LIKE :entryNumberApartement THEN (:entryNumberApartement)ELSE numberApartment END),"+
            "numberAndStreet = (CASE WHEN numberAndStreet NOT LIKE :entryNumberAndStreet THEN (:entryNumberAndStreet)ELSE numberAndStreet END),"+
            "city = (CASE WHEN city NOT LIKE :entryCity THEN (:entryCity)ELSE city END),"+
            "region = (CASE WHEN region NOT LIKE :entryRegion THEN (:entryRegion)ELSE region END),"+
            "postalCode = (CASE WHEN postalCode NOT LIKE :entryPostalCode THEN (:entryPostalCode)ELSE postalCode END),"+
            "country = (CASE WHEN country NOT LIKE :entryCountry THEN (:entryCountry)ELSE country END)"+
            "WHERE id = :id ")
    suspend fun updateRealEstate(
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
        entryCountry: String
    )

    @Query("UPDATE  RealEstateDatabase SET " +
            "lat = (CASE WHEN lat NOT LIKE :lat THEN (:lat) ELSE lat END)," +
            "lng = (CASE WHEN lng NOT LIKE :lng THEN (:lng) ELSE lng END)"+
            "WHERE id = :id ")
    suspend fun updateRealEstateLatLng(
        id: String,
        lat: Double?,
        lng: Double?,
    )

    //"UPDATE  RealEstateDatabase SET type = :entryType WHERE id = :id AND type NOT LIKE :entryType "
    //"UPDATE  RealEstateDatabase SET " +
    //            "type = (CASE WHEN type NOT LIKE :entryType THEN (:entryType) ELSE type END) ," +
    //            "price = (CASE WHEN price NOT LIKE :entryPrice THEN (:entryPrice) ELSE price END) " +
    //            "WHERE id =:id"

    @RawQuery(observedEntities = [RealEstateDatabase::class])
    fun getPropertyBySearch(supportSQLiteQuery: SupportSQLiteQuery): LiveData<List<RealEstateDatabase>>

}