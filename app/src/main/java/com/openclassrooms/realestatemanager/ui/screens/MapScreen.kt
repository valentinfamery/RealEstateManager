package com.openclassrooms.realestatemanager.ui.screens

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Looper
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    realEstateViewModel: RealEstateViewModel
) {


    lateinit var mLocationRequest : LocationRequest
    lateinit var locationCallback: LocationCallback
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    val activity = LocalContext.current as Activity
    val context = LocalContext.current

    var userPosition by remember {
        mutableStateOf(LatLng(37.422131,-122.084801))
    }


    locationCallback = object : LocationCallback() {
        override fun onLocationResult(p0: LocationResult) {
            for (location in p0.locations){
                Log.e(location.latitude.toString(),location.longitude.toString())
                userPosition = LatLng(location.latitude,location.longitude)
                Log.e("userPosition",userPosition.toString())

                // Update UI with location data
                // ...
            }
        }
    }

    fun startLocationUpdates() {
        mLocationRequest = LocationRequest.create().apply {
            interval = 100
            fastestInterval = 50
            priority =  Priority.PRIORITY_HIGH_ACCURACY
            maxWaitTime = 100
        }

        // Create LocationSettingsRequest object using location request

        fusedLocationProviderClient = getFusedLocationProviderClient(activity)

        val builder : LocationSettingsRequest.Builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(mLocationRequest)
        val locationSettingsRequest : LocationSettingsRequest = builder.build()
        val settingsClient : SettingsClient = LocationServices.getSettingsClient(activity)
        settingsClient.checkLocationSettings(locationSettingsRequest)


        fusedLocationProviderClient.requestLocationUpdates(mLocationRequest, locationCallback ,Looper.myLooper())

    }

    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = {
            if (it) {
                startLocationUpdates()
            } else {
                Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
            }
        },
    )



        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED -> {
                startLocationUpdates()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(activity, "") -> {}
            else -> {
                SideEffect {
                    requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                }

            }
        }





    ConstraintLayout {
        val (centerAlignedTopAppBar,map) = createRefs()
        CenterAlignedTopAppBar(
            title = {
                Text(text = "Map")
            },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                }) {
                    Icon(Icons.Filled.Menu,"")
                }
            },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Search, contentDescription = "Localized description")
                }
            },
            modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                top.linkTo(parent.top, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        )

        val cameraPositionState = rememberCameraPositionState {
            position = CameraPosition.fromLatLngZoom(userPosition, 10f)
        }


        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .constrainAs(map) {
                    top.linkTo(centerAlignedTopAppBar.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },


            cameraPositionState = cameraPositionState
        ) {


            Marker(
                state = MarkerState(position = userPosition),
            )

        }


    }

}