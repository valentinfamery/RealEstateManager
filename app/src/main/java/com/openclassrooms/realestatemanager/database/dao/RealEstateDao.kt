package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.domain.models.Estate
import kotlinx.coroutines.flow.Flow

@Dao
interface RealEstateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRealEstate(realEstate: Estate)

    @Query("SELECT * FROM Estate")
    fun realEstates(): Flow<List<Estate>>

    @Query("DELETE FROM Estate")
    suspend fun clear()



    @Query("SELECT * FROM Estate WHERE id = :realEstateId")
    fun realEstateById(realEstateId : String):LiveData<Estate?>

    @Query("SELECT * FROM Estate")
    fun getRealEstatesWithCursor(): Cursor

    @Update
    suspend fun updateEstate(estate: Estate)


    @RawQuery(observedEntities = [Estate::class])
    fun getPropertyBySearch(supportSQLiteQuery: SupportSQLiteQuery): LiveData<List<Estate>>

}