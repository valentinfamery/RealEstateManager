package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateDatabase

@Dao
interface RealEstateDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertRealEstate(realEstate: RealEstateDatabase)

    @Query("SELECT * FROM RealEstateDatabase")
    fun realEstates(): List<RealEstateDatabase>

    @Query("DELETE FROM RealEstateDatabase")
    fun clear()

}