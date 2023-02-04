package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri
import com.openclassrooms.realestatemanager.database.EstateRoomDatabase
import com.openclassrooms.realestatemanager.domain.models.Estate


class RealEstatesContentProvider : ContentProvider() {

    val AUTHORITY = "com.openclassrooms.realestatemanager.provider"

    val TABLE_NAME = Estate::class.java.simpleName

    val URI_REAL_ESTATE = Uri.parse("content://$AUTHORITY/$TABLE_NAME")


    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        p0: Uri,
        p1: Array<out String>?,
        p2: String?,
        p3: Array<out String>?,
        p4: String?
    ): Cursor {

        if (context != null) {
            val cursor : Cursor = EstateRoomDatabase.getInstance(context!!).realEstateDao().getRealEstatesWithCursor()
            cursor.setNotificationUri(context!!.contentResolver, p0);
            return cursor
        }
        TODO("Not yet implemented")
    }

    override fun getType(p0: Uri): String? {
        TODO("Not yet implemented")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        TODO("Not yet implemented")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        TODO("Not yet implemented")
    }

}