package com.openclassrooms.realestatemanager.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
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
import com.openclassrooms.realestatemanager.ui.FilterActivity
import com.openclassrooms.realestatemanager.ui.components.TopBar
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

    LaunchedEffect(boolean) {
        if (boolean) {
            navControllerDrawer.navigate("detailScreen")
            boolean = false
        }
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val activity = LocalContext.current as Activity

    val realEstates by realEstateViewModel.realEstates.collectAsState()

    var userPosition by remember {
        mutableStateOf(LatLng(40.815959, -73.8192879))
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



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopBar(
            title = "Map",
            backNavigate = false,
            filterScreen = false,
            drawerButton = true,
            navigateToFilterScreen = {

            },
            navigateToBack = { /*TODO*/ },
            modifier = Modifier,
            openDrawer = {
                scope.launch { drawerState.open() }
            }
        )

        if (locationPermissionState.status.isGranted) {
            startLocationUpdates()

            if (userPosition == LatLng(40.815959, -73.8192879)) {
                Spacer(modifier = Modifier.fillMaxSize(0.025f))
                Text(text = "This location is the default please check google plays services for the real location")
                Spacer(modifier = Modifier.fillMaxSize(0.025f))
            }

            val cameraPositionState = rememberCameraPositionState {
                position = CameraPosition.fromLatLngZoom(userPosition, 10f)
            }

            val mapProperties by remember { mutableStateOf(MapProperties(isMyLocationEnabled = true)) }
            val uiSettings by remember { mutableStateOf(MapUiSettings(myLocationButtonEnabled = true)) }

            GoogleMap(
                cameraPositionState = cameraPositionState,
                properties = mapProperties,
                uiSettings = uiSettings,
            ) {
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

            }

        } else {
            Spacer(modifier = Modifier.fillMaxSize(0.025f))
            Text(text = "Please allow location access")
            Spacer(modifier = Modifier.fillMaxSize(0.025f))
            Button(onClick = {
                locationPermissionState.launchPermissionRequest()
            }) {
                Text(text = "Allow location access")
            }
        }


    }

}




