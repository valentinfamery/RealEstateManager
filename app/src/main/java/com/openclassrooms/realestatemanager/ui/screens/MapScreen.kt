package com.openclassrooms.realestatemanager.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.NavHostController
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
import com.google.gson.Gson
import com.google.maps.android.compose.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.utils.WindowSize

import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.utils.WindowType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    realEstateViewModel: RealEstateViewModel,
    navControllerDrawer: NavController,
    windowSize: WindowSize
) {




    if(windowSize.width != WindowType.Expanded) {

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
                                Text(text = "Map")
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Filled.Menu, "")
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
                                    title = realEstate.numberAndStreet + " " + realEstate.city + " " + realEstate.postalCode + " " + realEstate.region + " " + realEstate.country,
                                    onInfoWindowClick = {
                                        realEstateViewModel.realEstateIdDetail.value = realEstate.id
                                        boolean = true
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
                    text = "Impossible de recupérer la localisation des services google play verifier que une localisation a été enregistré dans ceux ci ",
                    modifier = Modifier.constrainAs(textError) {
                        top.linkTo(centerAlignedTopAppBar.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    })

            }
        }

    } else {

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
                                Text(text = "Map")
                            },
                            navigationIcon = {
                                IconButton(onClick = {
                                    scope.launch { drawerState.open() }
                                }) {
                                    Icon(Icons.Filled.Menu, "")
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
                                    title = "title",
                                    onInfoWindowClick = {
                                        realEstateViewModel.realEstateIdDetail.value = realEstate.id

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
                    text = "Impossible de recupérer la localisation des services google play verifier que une localisation a été enregistré dans ceux ci ",
                    modifier = Modifier.constrainAs(textError) {
                        top.linkTo(centerAlignedTopAppBar.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                        bottom.linkTo(parent.bottom, margin = 0.dp)
                    })

            }
        }

    }

}




