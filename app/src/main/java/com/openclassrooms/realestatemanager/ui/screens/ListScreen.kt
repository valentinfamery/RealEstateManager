package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
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
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.Utils
import com.openclassrooms.realestatemanager.models.FilterResult
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.ui.FilterActivity
import com.openclassrooms.realestatemanager.utils.Resource
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
    navControllerTwoPane: NavHostController,
) {
    val context = LocalContext.current

    val items by realEstateViewModel.uiState(Utils.isInternetAvailable(context)).collectAsState()

    var listFilter = mutableListOf<RealEstateDatabase>()

    var filterState by remember { mutableStateOf(false) }

    lateinit var resultFilter : FilterResult

    var refreshing by remember { mutableStateOf(false) }

    val launcherActivityResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data

                    filterState = true


                resultFilter = data?.getParcelableExtra("resultFilter")!!

            }
        }

    if (windowSize == WindowSize.COMPACT) {
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
                            launcherActivityResult.launch(
                                Intent(
                                    context,
                                    FilterActivity::class.java
                                )
                            )
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
                SwipeRefresh(
                    modifier = Modifier.padding(it),
                    state = rememberSwipeRefreshState(refreshing),
                    onRefresh = {
                        realEstateViewModel.refreshRealEstates(Utils.isInternetAvailable(context))
                                },
                ) {
                    LazyColumn(
                    ) {
                        if (filterState == false) {
                            when (items) {
                                is Resource.Loading -> {
                                }
                                is Resource.Success -> {
                                    Log.e("items", "listScreen")
                                    items.data?.let { items ->
                                        items(items) { item ->
                                            RowList(
                                                item,
                                                realEstateViewModel,
                                                navControllerDrawer,
                                                windowSize,
                                                navControllerTwoPane
                                            )
                                        }
                                    }
                                }
                                else -> {}
                            }


                        } else {
                            listFilter.filter {realEstate->
                                realEstate.type.equals(resultFilter.type)
                                realEstate.city.equals(resultFilter.city)
                                realEstate.schoolsNear == resultFilter.schools
                                realEstate.shopsNear == resultFilter.shops
                            }

                            items(listFilter) { item ->
                                RowList(
                                    item,
                                    realEstateViewModel,
                                    navControllerDrawer,
                                    windowSize,
                                    navControllerTwoPane
                                )
                            }
                        }
                    }
                }
            }
        )
    } else {

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
                    when (items) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            items.data?.let {items->
                                items(items) { item ->
                                    RowList(
                                        item,
                                        realEstateViewModel,
                                        navControllerDrawer,
                                        windowSize,
                                        navControllerTwoPane
                                    )
                                }
                            }
                        }
                    }
                }
            }
        )
    }

}




