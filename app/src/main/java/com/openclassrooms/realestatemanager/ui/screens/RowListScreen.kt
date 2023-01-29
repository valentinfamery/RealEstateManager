package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@Composable
fun RowList(
    item: RealEstateDatabase,
    navController: NavController,
    isExpanded : Boolean,
    realEstateViewModel: RealEstateViewModel
) {


    if (!isExpanded) {
        val items2 = item.listPhotoWithText

        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row(Modifier.clickable {
                realEstateViewModel.realEstateIdDetail.value = item.id
                navController.navigate("detailScreen")

            }) {

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(84.dp)
                        .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {
                    if (items2?.get(0)?.photoSource != null) {
                        GlideImage(
                            imageModel = {items2[0].photoSource},
                            imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                        )
                    }
                }



                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = item.type.toString(),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "$"+ item.price.toString(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }





    } else  {
        val items2 = item.listPhotoWithText

        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row(Modifier.clickable {
                realEstateViewModel.realEstateIdDetail.value = item.id
            }) {

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(84.dp)
                        .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {
                    GlideImage(
                        imageModel = {items2?.get(0)?.photoSource},
                        imageOptions = ImageOptions(contentScale = ContentScale.Crop),
                    )
                }



                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                ) {
                    Text(
                        text = item.type.toString(),
                        style = MaterialTheme.typography.headlineLarge
                    )
                    Text(
                        text = "$" + item.price.toString(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }


}


