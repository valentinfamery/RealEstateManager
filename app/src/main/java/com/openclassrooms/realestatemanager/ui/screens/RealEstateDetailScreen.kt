package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.ui.components.TopBar
import com.openclassrooms.realestatemanager.utils.Utils
import com.skydoves.landscapist.ImageOptions
import com.skydoves.landscapist.glide.GlideImage

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RealEstateDetailScreen(
    navigateToEditScreen: () -> Unit,
    navigateToEditScreenExpanded: () -> Unit,
    isExpanded: Boolean,
    itemRealEstate: RealEstateDatabase?,
    navigateToBack : () -> Unit
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
                                navigateToEditScreen()
                                navigateToEditScreenExpanded()

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

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        TopBar(
                            title = "Estate",
                            backNavigate = !isExpanded,
                            filterScreen = false,
                            drawerButton = false,
                            navigateToFilterScreen = { /*TODO*/ },
                            navigateToBack = { navigateToBack() },
                            openDrawer = { /*TODO*/ },
                            modifier = Modifier
                        )

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        FlowRow(modifier = Modifier
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
                                                .aspectRatio(0.9f)
                                                .clip(RoundedCornerShape(15.dp)),
                                            imageOptions = ImageOptions(contentScale = ContentScale.FillBounds)
                                        )

                                    Text(text = listPhotos?.get(it)?.text ?: "")
                                }
                            }
                        }
                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        FlowRow(
                            modifier = Modifier
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

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Box(modifier = Modifier
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

                        Spacer(modifier = Modifier.fillMaxHeight(0.10f))

                        Box(
                            modifier = Modifier
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

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        FlowRow(
                            modifier = Modifier
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

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = "Status : " + itemRealEstate.status.toString())
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Row(modifier = Modifier
                            .fillMaxWidth(0.8f)) {
                            Text(text = "Date of Entry : " + itemRealEstate.dateOfEntry.toString())
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Row(modifier = Modifier
                            .fillMaxWidth(0.8f)) {
                            if(itemRealEstate.status == "Sold") {
                                Text(text = "Date of Sale : " + itemRealEstate.dateOfSale.toString())
                            }
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Row() {
                            Text(text = "Agent : "+itemRealEstate.realEstateAgent.toString())
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))


                        if (itemRealEstate.lat != null && itemRealEstate.lng != null) {

                            val latLng = LatLng(
                                itemRealEstate.lat!!, itemRealEstate.lng!!
                            )

                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(latLng, 10f)
                            }



                            GoogleMap(cameraPositionState = cameraPositionState, modifier = Modifier
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

