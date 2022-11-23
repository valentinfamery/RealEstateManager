package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
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
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.utils.WindowSize
import com.openclassrooms.realestatemanager.utils.WindowType
import com.skydoves.landscapist.glide.GlideImage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateDetailScreen(
    realEstateViewModel: RealEstateViewModel,
    navController: NavController,
    windowSize: WindowSize
) {
    val realEstateid by realEstateViewModel.realEstateId.collectAsState()

    Log.e("itemRealEstateId", realEstateid)

    if (realEstateid != "") {

        val itemRealEstate by realEstateViewModel.realEstateById(realEstateid).collectAsState()



        itemRealEstate?.let {itemRealEstate->

            val listPhotos = itemRealEstate.listPhotoWithText


            Scaffold(
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = {

                            val item = Uri.encode(Gson().toJson(itemRealEstate))

                            navController.navigate("editScreen/$item")


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
                            .fillMaxHeight(0.20f),
                            mainAxisSpacing = 10.dp
                        ) {
                            repeat(listPhotos?.size ?: 0) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    GlideImage(
                                        imageModel = listPhotos?.get(it)?.photoUrl,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .clickable {
                                                val photoUrl =
                                                    Uri.encode(Gson().toJson(listPhotos?.get(it)?.photoUrl))
                                                navController.navigate("PictureDetail/$photoUrl")
                                            }
                                            .clip(RoundedCornerShape(15.dp))
                                            .fillMaxWidth(0.40f)
                                            .fillMaxHeight(0.20f)
                                    )
                                    Text(text = listPhotos?.get(it)?.text ?: "")
                                }
                            }
                        }



                        Row(
                            modifier = Modifier
                                .constrainAs(textType) {
                                    top.linkTo(lazyColumnPhoto.bottom, margin = 25.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                                .fillMaxWidth(0.8f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = itemRealEstate.type.toString())
                            Text(text = itemRealEstate.price.toString() + " $")
                        }


                        Row(
                            modifier = Modifier
                                .constrainAs(textArea) {
                                    top.linkTo(textType.bottom, margin = 25.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                                .fillMaxWidth(0.8f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_crop_square_24),
                                contentDescription = ""
                            )
                            Text(text = itemRealEstate.area.toString() + " m2")
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_house_24),
                                contentDescription = ""
                            )
                            Text(text = itemRealEstate.numberRoom.toString() + " Rooms")
                        }

                        Row(modifier = Modifier
                            .constrainAs(textDescription) {
                                top.linkTo(textArea.bottom, margin = 25.dp)
                                start.linkTo(parent.start, margin = 0.dp)
                                end.linkTo(parent.end, margin = 0.dp)
                            }
                            .fillMaxWidth(0.8f)) {
                            Text(text = itemRealEstate.description.toString())
                        }

                        Row(
                            modifier = Modifier
                                .constrainAs(textAddress) {
                                    top.linkTo(textDescription.bottom, margin = 25.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                                .fillMaxWidth(0.8f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Icon(Icons.Filled.LocationOn, contentDescription = "")
                            Text(text = itemRealEstate.numberAndStreet + " " + itemRealEstate.city + " " + itemRealEstate.postalCode + " " + itemRealEstate.region + " " + itemRealEstate.country)
                        }

                        Row(
                            modifier = Modifier
                                .constrainAs(rowHospital) {
                                    top.linkTo(textAddress.bottom, margin = 5.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                                .fillMaxWidth(0.8f),
                            verticalAlignment = Alignment.CenterVertically,

                            ) {

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

                        Row(
                            modifier = Modifier
                                .constrainAs(rowShops) {
                                    top.linkTo(rowHospital.bottom, margin = 5.dp)
                                    start.linkTo(parent.start, margin = 0.dp)
                                    end.linkTo(parent.end, margin = 0.dp)
                                }
                                .fillMaxWidth(0.8f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Checkbox(
                                checked = itemRealEstate.shopsNear,
                                onCheckedChange = { itemRealEstate.shopsNear = it }
                            )
                            Text(text = "Near Shops")
                            itemRealEstate?.let { it1 ->
                                Checkbox(
                                    checked = it1.parksNear,
                                    onCheckedChange = { it1.parksNear = it }
                                )
                            }
                            Text(text = "Near Parks")
                        }

                        Row(
                            modifier = Modifier
                                .constrainAs(textStatus) {
                                    top.linkTo(rowShops.bottom, margin = 25.dp)
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
                            Text(text = itemRealEstate.dateOfSale.toString())
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





}

