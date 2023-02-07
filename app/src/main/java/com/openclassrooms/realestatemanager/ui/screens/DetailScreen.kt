package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
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

                    ConstraintLayout(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxWidth()) {

                        val (
                            topBar,flowPhotos,infoType,infoPrice,infoSurface,
                            infoNumberRooms,boxDescription,boxAddress,
                            infoHospital,infoSchools,infoShops,infoParks,
                            infoStatus,infoDateEntry,infoDateSale,infoAgent
                        ) = createRefs()
                        val (map) = createRefs()
                        TopBar(
                            title = Screen.DetailScreen.title,
                            backNavigate = !isExpanded,
                            filterScreen = false,
                            drawerButton = false,
                            navigateToFilterScreen = { /*TODO*/ },
                            navigateToBack = { navigateToBack() },
                            openDrawer = { /*TODO*/ },
                            modifier = Modifier.constrainAs(topBar){
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.matchParent
                                height = Dimension.wrapContent

                            }
                        )

                        FlowRow(modifier = Modifier.constrainAs(flowPhotos){
                            top.linkTo(topBar.bottom,10.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.percent(0.8f)
                            height = Dimension.wrapContent
                        }
                        ) {
                            listPhotos.forEach {
                                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
                                    .fillMaxWidth(0.49f)
                                    .padding(5.dp)) {

                                        GlideImage(
                                            imageModel = { it.photoSource },
                                            modifier = Modifier
                                                .aspectRatio(0.9f)
                                                .clip(RoundedCornerShape(15.dp)),
                                            imageOptions = ImageOptions(contentScale = ContentScale.FillBounds)
                                        )

                                    Text(text = it.text)
                                }
                            }
                        }

                        Text(text = stringResource(R.string.beforeType) + estate.type.toString(), modifier = Modifier.constrainAs(infoType){
                            top.linkTo(flowPhotos.bottom,15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
                        Text(text = stringResource(R.string.beforePrice) + estate.price.toString() + stringResource(R.string.dollarSymbol),modifier = Modifier.constrainAs(infoPrice){
                            top.linkTo(infoType.bottom,15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
                        Text(text = stringResource(R.string.beforeSurface) + estate.area.toString() + stringResource(R.string.surfaceUnity),modifier = Modifier.constrainAs(infoSurface){
                            top.linkTo(infoPrice.bottom,15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
                        Text(text = stringResource(R.string.beforeNumberRooms) + estate.numberRoom.toString(), modifier = Modifier.constrainAs(infoNumberRooms){
                            top.linkTo(infoSurface.bottom,15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })

                        Box(modifier = Modifier.constrainAs(boxDescription){
                            top.linkTo(infoNumberRooms.bottom,15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            width = Dimension.percent(0.8f)
                            height = Dimension.wrapContent
                        }
                            .clip(RoundedCornerShape(10.dp))
                            .background(MaterialTheme.colorScheme.surfaceColorAtElevation(1.dp))) {
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(text = stringResource(R.string.beforeDescription))
                                Spacer(modifier = Modifier.size(10.dp))
                                Text(text = estate.description.toString())
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .constrainAs(boxAddress){
                                    top.linkTo(boxDescription.bottom,15.dp)
                                    start.linkTo(parent.start)
                                    end.linkTo(parent.end)
                                    width = Dimension.percent(0.8f)
                                    height = Dimension.wrapContent
                                }
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

                        FilterChip(
                            modifier = Modifier.constrainAs(infoHospital){
                                top.linkTo(boxAddress.bottom,15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.8f)
                                height = Dimension.wrapContent
                            },
                            selected = estate.hospitalsNear,
                            onClick = { estate.hospitalsNear = !estate.hospitalsNear },
                            label = { Text(stringResource(R.string.editInfoHospital)) },
                            leadingIcon = if (estate.hospitalsNear) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            },
                        )

                        FilterChip(
                            modifier = Modifier.constrainAs(infoSchools){
                                top.linkTo(infoHospital.bottom,15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.8f)
                                height = Dimension.wrapContent
                            },
                            selected = estate.schoolsNear,
                            onClick = { estate.schoolsNear = !estate.schoolsNear },
                            label = { Text(stringResource(R.string.editInfoSchool)) },
                            leadingIcon = if (estate.schoolsNear) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            }
                        )
                        FilterChip(
                            modifier = Modifier.constrainAs(infoShops){
                                top.linkTo(infoSchools.bottom,15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.8f)
                                height = Dimension.wrapContent
                            },
                            selected = estate.shopsNear,
                            onClick = { estate.shopsNear = !estate.shopsNear },
                            label = { Text(stringResource(R.string.editInfoShops)) },
                            leadingIcon = if (estate.shopsNear) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            }
                        )
                        FilterChip(
                            modifier = Modifier.constrainAs(infoParks){
                                top.linkTo(infoShops.bottom,15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
                                width = Dimension.percent(0.8f)
                                height = Dimension.wrapContent
                            },
                            selected = estate.parksNear,
                            onClick = { estate.parksNear = !estate.parksNear },
                            label = { Text(stringResource(R.string.editInfoParks)) },
                            leadingIcon = if (estate.parksNear) {
                                {
                                    Icon(
                                        imageVector = Icons.Filled.Done,
                                        contentDescription = "",
                                        modifier = Modifier.size(FilterChipDefaults.IconSize)
                                    )
                                }
                            } else {
                                null
                            }
                        )


                        Text(text = stringResource(R.string.beforeStatus) + estate.status.toString(), modifier = Modifier.constrainAs(infoStatus){
                            top.linkTo(infoParks.bottom,15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })
                        Text(text = stringResource(R.string.beforeDateOfEntry) + estate.dateOfEntry.toString(), modifier = Modifier.constrainAs(infoDateEntry){
                            top.linkTo(infoStatus.bottom,15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })


                        Text(text = if(estate.status == "Sold") stringResource(R.string.beforeDateOfSale) + estate.dateOfSale.toString() else "", modifier = Modifier.constrainAs(infoDateSale){
                            top.linkTo(infoDateEntry.bottom,15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })

                        Text(text = stringResource(R.string.beforeAgent) + estate.realEstateAgent.toString(),modifier = Modifier.constrainAs(infoAgent){
                            top.linkTo(infoDateSale.bottom,15.dp)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        })


                        if (estate.lat != null && estate.lng != null) {

                            val latLng = LatLng(
                                estate.lat!!, estate.lng!!
                            )

                            val cameraPositionState = rememberCameraPositionState {
                                position = CameraPosition.fromLatLngZoom(latLng, 10f)
                            }



                            GoogleMap(cameraPositionState = cameraPositionState, modifier = Modifier.constrainAs(map){
                                top.linkTo(infoAgent.bottom,15.dp)
                                start.linkTo(parent.start)
                                end.linkTo(parent.end)
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

