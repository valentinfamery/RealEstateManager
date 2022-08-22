package com.openclassrooms.realestatemanager.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.models.RealEstate

@Dao
interface RealEstateDao {

    @Insert
    fun insertRealEstate(realEstate: RealEstate)

    @Query("SELECT * FROM RealEstate")
    fun realEstates(): List<RealEstate>

    @Query("DELETE FROM RealEstate")
    fun clear()

}