package com.openclassrooms.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.net.Uri
import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.RealEstateRoomDatabase
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class ContentProviderTest
{
    private var mContentResolver: ContentResolver? = null
    private val USER_ID: Long = 1

    val AUTHORITY = "com.openclassrooms.realestatemanager.provider"

    val TABLE_NAME = RealEstateDatabase::class.java.simpleName

    val URI_REAL_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

    @Before
    fun setUp() {
        Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            RealEstateRoomDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()
        mContentResolver = InstrumentationRegistry.getInstrumentation().getContext()
            .getContentResolver()
    }

    @Test
    fun getItemsWhenNoItemInserted() {
        val cursor = mContentResolver!!.query(
            ContentUris.withAppendedId(
                URI_REAL_ESTATE,
                USER_ID
            ), null, null, null, null
        )
        assertThat(cursor, notNullValue())
        assertThat(cursor!!.count, `is`(1))
        cursor.close()
    }

}