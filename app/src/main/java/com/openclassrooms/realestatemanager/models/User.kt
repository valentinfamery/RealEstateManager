package com.openclassrooms.realestatemanager.models

class User {
    // --- GETTERS ---
    var uid: String? = null

    // --- SETTERS ---
    var username: String? = null
    var urlPicture: String? = null
    var email: String? = null

    constructor(uid: String?, username: String?, urlPicture: String?, email: String?) {
        this.uid = uid
        this.username = username
        this.urlPicture = urlPicture
        this.email = email
    }
}