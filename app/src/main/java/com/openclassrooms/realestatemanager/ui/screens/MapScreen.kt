package com.openclassrooms.realestatemanager.ui.screens

import android.Manifest
import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.Estate
import com.openclassrooms.realestatemanager.presentation.viewModels.EstateViewModel
import com.openclassrooms.realestatemanager.ui.components.TopBar
import com.openclassrooms.realestatemanager.utils.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    estateViewModel: EstateViewModel,
    navControllerDrawer: NavController,
    isExpanded: Boolean
) {

    var boolean by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(boolean) {
        if (boolean) {
            navControllerDrawer.navigate(Screen.DetailScreen.route)
            boolean = false
        }
    }

    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    val activity = LocalContext.current as Activity

    val realEstates by estateViewModel.realEstates.collectAsState()

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



    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        TopBar(
            title = Screen.MapScreen.title,
            backNavigate = false,
            filterScreen = false,
            drawerButton = !isExpanded,
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

            if (userPosition != LatLng(0.0, 0.0)) {

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

                            val realEstate: Estate = it


                            if (it.lat != null && it.lng != null) {

                                val latLng = LatLng(
                                    it.lat!!, it.lng!!
                                )
                                Marker(
                                    state = MarkerState(position = latLng),
                                    title = "${realEstate.numberAndStreet} ${realEstate.city} ${realEstate.postalCode} ${realEstate.region} ${realEstate.country}",
                                    onInfoWindowClick = {
                                        estateViewModel.realEstateIdDetail.value = realEstate.id
                                        if (!isExpanded) {
                                            boolean = true
                                        }
                                    }
                                )
                            }
                        }

                    }

                }

            }else {
                Spacer(modifier = Modifier.fillMaxSize(0.025f))
                Text(text = stringResource(R.string.locationPlayHistoryIsEmpty))
                Spacer(modifier = Modifier.fillMaxSize(0.025f))
            }

        } else {
            Spacer(modifier = Modifier.fillMaxSize(0.025f))
            Text(text = stringResource(R.string.infoLocationAllow))
            Spacer(modifier = Modifier.fillMaxSize(0.025f))
            Button(onClick = {
                locationPermissionState.launchPermissionRequest()
            }) {
                Text(text = stringResource(R.string.buttonAllowLocation))
            }
        }


    }

}




