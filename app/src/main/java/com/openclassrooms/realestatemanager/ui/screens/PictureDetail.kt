package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavHostController
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun PictureDetail(photoUrl: String?, navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize()) {
        GlideImage(
            imageModel = photoUrl,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxHeight(0.9f).fillMaxWidth(1f)
        )
    }
}