package com.openclassrooms.realestatemanager.ui.screens

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    realEstateViewModel: RealEstateViewModel,
    navControllerDrawer: NavController,
    isExpanded: Boolean
) {






        var boolean by remember {
            mutableStateOf(false)
        }

        LaunchedEffect(boolean){
            if(boolean){
                navControllerDrawer.navigate("detailScreen")
                boolean = false
            }
        }


        val navController = rememberNavController()
        lateinit var fusedLocationProviderClient: FusedLocationProviderClient
        val activity = LocalContext.current as Activity

        val realEstates by realEstateViewModel.realEstates.collectAsState()

        var userPosition by remember {
            mutableStateOf(LatLng(0.0, 0.0))
        }


        fun startLocationUpdates() {
            fusedLocationProviderClient = getFusedLocationProviderClient(activity)
            fusedLocationProviderClient.lastLocation.addOnSuccessListener {

                if (it != null) {
                    userPosition = LatLng(it.latitude, it.longitude)
                }

            }
        }

        val locationPermissionState = rememberPermissionState(
            Manifest.permission.ACCESS_FINE_LOCATION
        )

        if (locationPermissionState.status.isGranted) {
            startLocationUpdates()
        } else {
            SideEffect {
                locationPermissionState.launchPermissionRequest()
            }
        }

        ConstraintLayout {
            val (centerAlignedTopAppBar, map, textError) = createRefs()


            NavHost(
                navController = navController,
                startDestination = "topBarMap",
                modifier = Modifier
                    .constrainAs(centerAlignedTopAppBar) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }
            ) {
                composable("topBarMap") {


                        CenterAlignedTopAppBar(
                            title = {
                                Text(text = stringResource(R.string.TitleTopAppBarMapScreen))
                            },
                            navigationIcon = {
                                if (!isExpanded) {
                                    IconButton(onClick = {
                                        scope.launch { drawerState.open() }
                                    }) {
                                        Icon(Icons.Filled.Menu, "")
                                    }
                                }
                            },


                            )
                }
            }

            if (userPosition != LatLng(0.0, 0.0)) {

                val cameraPositionState = rememberCameraPositionState {
                    position = CameraPosition.fromLatLngZoom(userPosition, 10f)
                }

                val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
                val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }

                GoogleMap(
                    modifier = Modifier
                        .fillMaxSize()
                        .constrainAs(map) {
                            top.linkTo(centerAlignedTopAppBar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    cameraPositionState = cameraPositionState,
                    properties = mapProperties,
                    uiSettings = uiSettings,
                ) {

                    //when (items) {
                    //is Response.Loading -> {
                    //}
                    //is Response.Success -> {
                    realEstates.let { items ->


                        items.forEach {

                            val realEstate: RealEstateDatabase = it


                            if (it.lat != null && it.lng != null) {

                                val latLng = LatLng(
                                    it.lat!!, it.lng!!
                                )
                                Marker(
                                    state = MarkerState(position = latLng),
                                    title = "${realEstate.numberAndStreet} ${realEstate.city} ${realEstate.postalCode} ${realEstate.region} ${realEstate.country}",
                                    onInfoWindowClick = {
                                        realEstateViewModel.realEstateIdDetail.value = realEstate.id
                                        if (!isExpanded) {
                                            boolean = true
                                        }
                                    }
                                )
                            }
                        }

                    }
                    //}
                    //else ->{}
                    //}

                }

            } else {

                Text(
                    text = stringResource(R.string.ErrorMapScreen),
                    modifier = Modifier.constrainAs(textError) {
                        top.linkTo(centerAlignedTopAppBar.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    })

            }
        }



}




