package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.gson.Gson
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.WindowSize
import com.openclassrooms.realestatemanager.utils.WindowType
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateDetailScreen(
    realEstateViewModel: RealEstateViewModel,
    navController: NavController,
    windowSize: WindowSize,
    itemRealEstate: RealEstateDatabase?
) {

    val context = LocalContext.current
        itemRealEstate?.let {itemRealEstate->

            val listPhotos = itemRealEstate.listPhotoWithText


            Scaffold(
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = {
                            val isInternetAvailable = Utils.isInternetAvailable(context)

                            if(isInternetAvailable) {

                                val item = Uri.encode(Gson().toJson(itemRealEstate))
                                navController.navigate("editScreen/$item")

                            }else{
                                Toast.makeText(context,"Impossible il n'y a pas de connexion Internet",
                                    Toast.LENGTH_LONG).show()
                            }


                        },
                        icon = { Icon(Icons.Filled.Edit, "Localized description") },
                        text = { Text(text = "Edit") },
                        modifier = Modifier.clip(RoundedCornerShape(15.dp))
                    )
                },
                content = {

                    ConstraintLayout(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxHeight()
                    ) {

                        val (centerAlignedTopAppBar, textType, textPrice, textArea, textNumberRoom, textDescription, textAddress, textStatus, textDateOfEntry, textDateOfSale, textRealEstateAgent, lazyColumnPhoto, googleMap) = createRefs()

                        val (rowHospital, rowSchool, rowShops, rowParks) = createRefs()

                        if(windowSize.width == WindowType.Compact || windowSize.width == WindowType.Medium ){
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(text = "Estate Manager")
                                },
                                navigationIcon = {
                                    IconButton(onClick = {
                                        navController.popBackStack()
                                    }) {
                                        Icon(Icons.Filled.ArrowBack, "")
                                    }
                                },
                                modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                                    top.linkTo(parent.top, margin = 0.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }

                            )
                        }else if (windowSize.width == WindowType.Expanded){
                            CenterAlignedTopAppBar(
                                title = {
                                    Text(text = "Estate Manager")
                                },
                                modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                                    top.linkTo(parent.top, margin = 0.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }

                            )
                        }

                        FlowRow(modifier = Modifier
                            .constrainAs(lazyColumnPhoto) {
                                top.linkTo(centerAlignedTopAppBar.bottom, margin = 25.dp)
                                start.linkTo(parent.start, margin = 25.dp)
                                end.linkTo(parent.end, margin = 25.dp)
                            }
                            .fillMaxWidth(0.80f)
                            .fillMaxHeight(0.10f)
                        ) {
                            repeat(listPhotos?.size ?: 0) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                                    .fillMaxWidth(0.50f)
                                    .padding(5.dp)) {

                                        GlideImage(
                                            imageModel = {listPhotos?.get(it)?.photoSource},
                                            modifier = Modifier
                                                .clickable {
                                                    val photoUrl =
                                                        Uri.encode(Gson().toJson(listPhotos?.get(it)?.photoSource))
                                                    navController.navigate("PictureDetail/$photoUrl")
                                                }
                                                .aspectRatio(0.9f)
                                                .clip(RoundedCornerShape(15.dp)),
                                            imageOptions = ImageOptions(contentScale = ContentScale.FillBounds)
                                        )

                                    Text(text = listPhotos?.get(it)?.text ?: "")
                                }
                            }
                        }



                        FlowRow(
                            modifier = Modifier
                                .constrainAs(textType) {
                                    top.linkTo(lazyColumnPhoto.bottom, margin = 25.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                                .fillMaxWidth(0.8f)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(0.4f)) {
                                Text(text = "Type : "+itemRealEstate.type.toString())
                                Text(text = "Price : "+itemRealEstate.price.toString() + " $")
                            }
                            Spacer(modifier = Modifier.fillMaxWidth(0.2f))
                            Column(modifier = Modifier.fillMaxWidth(0.4f)) {
                                Text(text = "Surface : "+itemRealEstate.area.toString() + " m2")
                                Text(text = "Number Rooms : "+itemRealEstate.numberRoom.toString())
                            }
                        }

                        Box(modifier = Modifier
                            .constrainAs(textDescription) {
                                top.linkTo(textType.bottom, margin = 25.dp)
                                start.linkTo(parent.start, margin = 0.dp)
                                end.linkTo(parent.end, margin = 0.dp)
                            }
                            .fillMaxWidth(0.8f)
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(text = "Description : ")
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(text = itemRealEstate.description.toString())
                            }
                        }

                        Box(
                            modifier = Modifier
                                .constrainAs(textAddress) {
                                    top.linkTo(textDescription.bottom, margin = 25.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                                .clip(RoundedCornerShape(10.dp))
                                .fillMaxWidth(0.8f)
                                .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp)),
                        ) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Row() {
                                    Icon(Icons.Filled.LocationOn, contentDescription = "")
                                    Text(text = " Address : ")
                                }
                                Text(text = itemRealEstate.numberAndStreet + " " + itemRealEstate.city + " " + itemRealEstate.postalCode + " " + itemRealEstate.region + " " + itemRealEstate.country)
                            }
                        }

                        FlowRow(
                            modifier = Modifier
                                .constrainAs(rowHospital) {
                                    top.linkTo(textAddress.bottom, margin = 25.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                                .fillMaxWidth(0.8f)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(0.4f)) {
                                Checkbox(
                                    checked = itemRealEstate.hospitalsNear,
                                    onCheckedChange = { itemRealEstate.hospitalsNear = it },
                                )
                                Text(text = "Near Hospital")
                                Checkbox(
                                    checked = itemRealEstate.schoolsNear,
                                    onCheckedChange = { itemRealEstate.schoolsNear = it }
                                )
                                Text(text = "Near School")
                            }
                            Spacer(modifier = Modifier.fillMaxWidth(0.2f))
                            Column(modifier = Modifier.fillMaxWidth(0.4f)) {
                                Checkbox(
                                    checked = itemRealEstate.shopsNear,
                                    onCheckedChange = { itemRealEstate.shopsNear = it }
                                )
                                Text(text = "Near Shops")
                                Checkbox(
                                    checked = itemRealEstate.parksNear,
                                    onCheckedChange = { itemRealEstate.parksNear = it }
                                )
                                Text(text = "Near Parks")
                            }
                        }


                        Row(
                            modifier = Modifier
                                .constrainAs(textStatus) {
                                    top.linkTo(rowHospital.bottom, margin = 25.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                                .fillMaxWidth(0.8f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = "Status : " + itemRealEstate.status.toString())
                        }

                        Row(modifier = Modifier
                            .constrainAs(textDateOfEntry) {
                                top.linkTo(textStatus.bottom, margin = 25.dp)
                                start.linkTo(parent.start, margin = 0.dp)
                                end.linkTo(parent.end, margin = 0.dp)
                            }
                            .fillMaxWidth(0.8f)) {
                            Text(text = "Date of Entry : " + itemRealEstate.dateOfEntry.toString())
                        }

                        Row(modifier = Modifier
                            .constrainAs(textDateOfSale) {
                                top.linkTo(textDateOfEntry.bottom, margin = 25.dp)
                                start.linkTo(parent.start, margin = 0.dp)
                                end.linkTo(parent.end, margin = 0.dp)
                            }
                            .fillMaxWidth(0.8f)) {
                            Text(text = "Date of Sale : " + itemRealEstate.dateOfSale.toString())
                        }

                        Row(modifier = Modifier.constrainAs(textRealEstateAgent) {
                            top.linkTo(textDateOfSale.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }) {
                            Text(text = itemRealEstate.realEstateAgent.toString())
                        }


                        if (itemRealEstate.lat != null && itemRealEstate.lng != null) {

                            val latLng = LatLng(
                                itemRealEstate.lat!!, itemRealEstate.lng!!
                            )

                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(latLng, 10f)
                            }



                            GoogleMap(cameraPositionState = cameraPositionState, modifier = Modifier
                                .constrainAs(googleMap) {
                                    top.linkTo(textRealEstateAgent.bottom, margin = 5.dp)
                                    start.linkTo(parent.start, margin = 5.dp)
                                    end.linkTo(parent.end, margin = 5.dp)
                                    bottom.linkTo(parent.bottom, margin = 75.dp)
                                }
                                .size(200.dp)
                                .clip(RoundedCornerShape(15.dp))
                            ) {
                                Marker(state = MarkerState(position = latLng)) {

                                }
                            }

                        }


                    }
                }
            )
        }







}

