package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.FilterActivity
import com.openclassrooms.realestatemanager.utils.WindowSize
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    realEstateViewModel: RealEstateViewModel,
    innerPadding: PaddingValues,
    navControllerDrawer: NavController,
    windowSize: WindowSize,
    navControllerTwoPane: NavHostController, ){

    val items by realEstateViewModel.uiState.collectAsState()

    val context = LocalContext.current

    val launcherActivityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        if(result.resultCode == Activity.RESULT_OK){
            val intent = result.data
        }
    }

    if(windowSize == WindowSize.COMPACT ){
        Scaffold(
            modifier = Modifier.padding(innerPadding),
            topBar = {

                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "RealEstateManager")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch { drawerState.open() }
                        }) {
                            Icon(Icons.Filled.Menu, "")
                        }
                    },
                    actions = {
                        IconButton(onClick = {



                            launcherActivityResult.launch(Intent(context, FilterActivity::class.java))
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                                contentDescription = ""
                            )
                        }
                    },
                )

            },
            content = {
                LazyColumn(
                    modifier = Modifier.padding(it),
                ) {
                    items(items) { item ->
                        RowList(item, realEstateViewModel,navControllerDrawer,windowSize,navControllerTwoPane)
                    }
                }
            }
        )
    }else {

        Scaffold(
            modifier = Modifier.padding(innerPadding),
            topBar = {

                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "RealEstates")
                    },
                    actions = {
                        IconButton(onClick = {
                            navControllerDrawer.navigate("filterScreen")
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                                contentDescription = ""
                            )
                        }
                    },
                )

            },
            content = {
                LazyColumn(
                    modifier = Modifier.padding(it),
                ) {
                    items(items) { item ->
                        RowList(item, realEstateViewModel, navControllerDrawer,windowSize,navControllerTwoPane)
                    }
                }
            }
        )
    }

}




