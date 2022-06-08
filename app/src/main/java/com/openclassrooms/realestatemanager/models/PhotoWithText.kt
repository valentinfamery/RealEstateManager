package com.openclassrooms.realestatemanager.models

class PhotoWithText(private var photoUrl : String? = null,private var text: String? = null) {

    fun getPhotoUrl(): String? {
        return photoUrl
    }

    fun getText(): String? {
        return text
    }
}