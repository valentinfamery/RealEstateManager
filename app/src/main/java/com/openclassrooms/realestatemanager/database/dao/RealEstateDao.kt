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

    @RawQuery(observedEntities = [RealEstateDatabase::class])
    fun getPropertyBySearch(supportSQLiteQuery: SupportSQLiteQuery): LiveData<List<RealEstateDatabase>>

}