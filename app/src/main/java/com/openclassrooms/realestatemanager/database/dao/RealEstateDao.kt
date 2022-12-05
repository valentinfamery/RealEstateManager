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
            "parksNear = (CASE WHEN parksNear NOT LIKE :parksNear THEn (:parksNear)ELSE parksNear END)" +
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
        parksNear: Boolean
    )

    //"UPDATE  RealEstateDatabase SET type = :entryType WHERE id = :id AND type NOT LIKE :entryType "
    //"UPDATE  RealEstateDatabase SET " +
    //            "type = (CASE WHEN type NOT LIKE :entryType THEN (:entryType) ELSE type END) ," +
    //            "price = (CASE WHEN price NOT LIKE :entryPrice THEN (:entryPrice) ELSE price END) " +
    //            "WHERE id =:id"

    @RawQuery(observedEntities = [RealEstateDatabase::class])
    fun getPropertyBySearch(supportSQLiteQuery: SupportSQLiteQuery): LiveData<List<RealEstateDatabase>>

}