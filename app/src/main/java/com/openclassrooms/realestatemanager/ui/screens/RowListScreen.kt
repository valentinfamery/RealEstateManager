package com.openclassrooms.realestatemanager.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.openclassrooms.realestatemanager.models.PhotoWithText
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.ui.RealEstateDetail
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import com.skydoves.landscapist.glide.GlideImage
import java.io.Serializable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowList(context: Context, item: RealEstate, realEstateViewModel: RealEstateViewModel) {

    val items2  by realEstateViewModel.getRealEstatePhotosWithId(item.getId().toString()).observeAsState()



    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row(Modifier.clickable {
            val intent = Intent(context, RealEstateDetail::class.java)
            intent.putExtra("itemId",item.getId())

            context.startActivity(intent)

        }) {

                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(84.dp)
                        .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                        .background(MaterialTheme.colorScheme.tertiary)
                ) {
                    GlideImage(
                        imageModel = items2?.get(0)?.getPhotoUrl(),
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
                    text = item.getType().toString(),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = item.getPrice().toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}


