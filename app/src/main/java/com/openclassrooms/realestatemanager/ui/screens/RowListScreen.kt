package com.openclassrooms.realestatemanager.ui.screens

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.utils.WindowSize
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowList(
    item: RealEstateDatabase,
    realEstateViewModel: RealEstateViewModel,
    navControllerDrawer: NavController,
    windowSize: WindowSize,
    navControllerTwoPane: NavHostController
) {

    if(windowSize == WindowSize.COMPACT){
        val items2  = item.listPhotoWithText

        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row(Modifier.clickable {
                val item = Uri.encode(Gson().toJson(item))

                navControllerDrawer.navigate("detailScreen/$item")

            }) {

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(84.dp)
                        .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {
                        if(items2?.get(0)?.photoUrl != null) {
                            GlideImage(
                                imageModel = items2[0].photoUrl,
                                contentScale = ContentScale.Crop,
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
                        text = "$",
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }else{
        val items2  = item.listPhotoWithText

        Card(
            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(corner = CornerSize(16.dp))
        ) {
            Row(Modifier.clickable {

                val item = Uri.encode(Gson().toJson(item))

                navControllerTwoPane.navigate("detailScreen/$item")
            }) {

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(84.dp)
                        .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {
                    GlideImage(
                        imageModel = items2?.get(0)?.photoUrl,
                        contentScale = ContentScale.Crop,
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
                        text = "$"+item.price.toString(),
                        style = MaterialTheme.typography.headlineSmall
                    )
                }
            }
        }
    }


}


