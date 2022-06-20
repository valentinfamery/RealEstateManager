package com.openclassrooms.realestatemanager.models

class PhotoWithTextFirebase(private var photoUrl : String? = null,private var text: String? =null) {

    fun getPhotoUrl(): String? {
        return photoUrl
    }

    fun getText(): String? {
        return text
    }

    fun setUrl(url: String?=null) {
        this.photoUrl = url
    }

}