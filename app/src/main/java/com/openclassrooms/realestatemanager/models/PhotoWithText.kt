package com.openclassrooms.realestatemanager.models

import android.net.Uri

data class PhotoWithText(var photoUrl : String? = null,var photoUri : Uri? = null,var text: String)