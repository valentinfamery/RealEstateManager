package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.models.RealEstateDatabase

@Dao
interface RealEstateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertRealEstate(realEstate: RealEstateDatabase)

    @Query("SELECT * FROM RealEstateDatabase")
    suspend fun realEstates(): List<RealEstateDatabase>

    @Query("DELETE FROM RealEstateDatabase")
    suspend fun clear()

    @Query("SELECT * FROM RealEstateDatabase")
    fun getRealEstatesWithCursor(): Cursor?

}