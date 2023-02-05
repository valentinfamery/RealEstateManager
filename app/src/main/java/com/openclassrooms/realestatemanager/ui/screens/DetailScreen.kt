package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.google.accompanist.flowlayout.FlowRow
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.Estate
import com.openclassrooms.realestatemanager.ui.components.TopBar
import com.openclassrooms.realestatemanager.utils.Screen
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
    itemRealEstate: Estate?,
    navigateToBack : () -> Unit,
    modifier: Modifier
) {

    val context = LocalContext.current
        itemRealEstate?.let {estate->

            val listPhotos = estate.listPhotoWithText

            val errorInternetNotAvailable = stringResource(R.string.ErrorInternetNotAvailable)
            val floatingActionButtonEdit = stringResource(R.string.FloatingActionButtonEdit)

            Scaffold(
                modifier = modifier,
                floatingActionButton = {
                    ExtendedFloatingActionButton(
                        onClick = {
                            val isInternetAvailable = Utils.isInternetAvailable()

                            if(isInternetAvailable) {
                                navigateToEditScreen()
                                navigateToEditScreenExpanded()

                            }else{
                                Toast.makeText(context,errorInternetNotAvailable, Toast.LENGTH_LONG).show()
                            }


                        },
                        icon = { Icon(Screen.EditScreen.icon, "") },
                        text = { Text(text = floatingActionButtonEdit) },
                        modifier = Modifier.clip(RoundedCornerShape(15.dp))
                    )
                },
                content = {

                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        TopBar(
                            title = Screen.DetailScreen.title,
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
                        ) {
                            repeat(listPhotos?.size ?: 0) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                                    .fillMaxWidth(0.49f)
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
                                Text(text = stringResource(R.string.beforeType) + estate.type.toString())
                                Text(text = stringResource(R.string.beforePrice) + estate.price.toString() + stringResource(R.string.dollarSymbol))
                            }
                            Spacer(modifier = Modifier.fillMaxWidth(0.2f))
                            Column(modifier = Modifier.fillMaxWidth(0.4f)) {
                                Text(text = stringResource(R.string.beforeSurface) + estate.area.toString() + stringResource(R.string.surfaceUnity))
                                Text(text = stringResource(R.string.beforeNumberRooms) + estate.numberRoom.toString())
                            }
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Box(modifier = Modifier
                            .fillMaxWidth(0.8f)
                            .wrapContentHeight()
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(text = stringResource(R.string.beforeDescription))
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(text = estate.description.toString())
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
                                Row {
                                    Icon(Icons.Filled.LocationOn, contentDescription = "")
                                    Text(text = stringResource(R.string.beforeAddress))
                                }
                                Text(text = "${estate.numberAndStreet.toString()}\n${estate.city}\n${estate.postalCode}\n${estate.region}\n${estate.country}")
                            }
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        FlowRow(
                            modifier = Modifier
                                .fillMaxWidth(0.8f)
                        ) {
                            Column(modifier = Modifier.fillMaxWidth(0.4f)) {
                                Checkbox(
                                    checked = estate.hospitalsNear,
                                    onCheckedChange = { estate.hospitalsNear = it },
                                )
                                Text(text = stringResource(R.string.beforeHospital))
                                Checkbox(
                                    checked = estate.schoolsNear,
                                    onCheckedChange = { estate.schoolsNear = it }
                                )
                                Text(text = stringResource(R.string.beforeSchool))
                            }
                            Spacer(modifier = Modifier.fillMaxWidth(0.2f))
                            Column(modifier = Modifier.fillMaxWidth(0.4f)) {
                                Checkbox(
                                    checked = estate.shopsNear,
                                    onCheckedChange = { estate.shopsNear = it }
                                )
                                Text(text = stringResource(R.string.beforeShops))
                                Checkbox(
                                    checked = estate.parksNear,
                                    onCheckedChange = { estate.parksNear = it }
                                )
                                Text(text = stringResource(R.string.beforeParks))
                            }
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth(0.8f),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(text = stringResource(R.string.beforeStatus) + estate.status.toString())
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Row(modifier = Modifier
                            .fillMaxWidth(0.8f)) {
                            Text(text = stringResource(R.string.beforeDateOfEntry) + estate.dateOfEntry.toString())
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Row(modifier = Modifier
                            .fillMaxWidth(0.8f)) {
                            if(estate.status == "Sold") {
                                Text(text = stringResource(R.string.beforeDateOfSale) + estate.dateOfSale.toString())
                            }
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))

                        Row {
                            Text(text = stringResource(R.string.beforeAgent) + estate.realEstateAgent.toString())
                        }

                        Spacer(modifier = Modifier.fillMaxHeight(0.025f))


                        if (estate.lat != null && estate.lng != null) {

                            val latLng = LatLng(
                                estate.lat!!, estate.lng!!
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

