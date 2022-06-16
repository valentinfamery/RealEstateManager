package com.openclassrooms.realestatemanager.models

import android.net.Uri

class PhotoWithText(private var photoUrl : String? = null,private var photoUri : Uri? = null,private var text: String) {

    fun getPhotoUrl(): String? {
        return photoUrl
    }

    fun getText(): String {
        return text
    }

    fun getPhotoUri(): Uri?{
        return photoUri
    }

    fun setUrl(url: String?=null) {
        this.photoUrl = url
    }

}