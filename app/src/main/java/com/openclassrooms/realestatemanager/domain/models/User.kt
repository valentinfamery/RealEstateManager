package com.openclassrooms.realestatemanager.domain.models

data class User(
    var uid: String? = "",
    var username: String? ="",
    var urlPicture: String? ="",
    var email: String? =""
)