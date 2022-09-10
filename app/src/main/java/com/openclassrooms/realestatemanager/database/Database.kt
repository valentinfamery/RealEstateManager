package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.utils.Converters

@Database(entities = [(RealEstateDatabase::class)], version = 1)
@TypeConverters(Converters::class)
abstract class RealEstateRoomDatabase: RoomDatabase() {

    abstract fun realEstateDao(): RealEstateDao

    companion object {

        private var INSTANCE: RealEstateRoomDatabase? = null

        fun getInstance(context: Context): RealEstateRoomDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        RealEstateRoomDatabase::class.java,
                        "product_database"
                    ).fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}