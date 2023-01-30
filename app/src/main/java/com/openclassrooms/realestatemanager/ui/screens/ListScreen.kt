package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
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
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.ui.FilterActivity
import com.openclassrooms.realestatemanager.ui.components.TopBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableState", "SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    realEstateViewModel: RealEstateViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
    isExpanded: Boolean
) {
    val context = LocalContext.current
    val realEstates by realEstateViewModel.realEstates.collectAsState()
    var filterState by remember { mutableStateOf(false) }
    var realEstatesFilter by remember { mutableStateOf(listOf<RealEstateDatabase>()) }

    val launcherActivityResult =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult ->
            if (activityResult.resultCode == 0) {
                filterState = activityResult.data?.getBooleanExtra("filterState", false)!!
            }
            if (activityResult.resultCode == 1) {
                filterState = activityResult.data?.getBooleanExtra("filterState", true)!!
                val type = activityResult.data?.getStringExtra("type")!!
                val city = activityResult.data?.getStringExtra("city")!!
                val minSurface = activityResult.data?.getIntExtra("minSurface", 0)!!
                val maxSurface = activityResult.data?.getIntExtra("maxSurface", 0)!!
                val minPrice = activityResult.data?.getIntExtra("minPrice", 0)!!
                val maxPrice = activityResult.data?.getIntExtra("maxPrice", 0)!!
                val onTheMarketLessALastWeek =
                    activityResult.data?.getBooleanExtra("onTheMarketLessALastWeek", false)!!
                val soldOn3LastMonth =
                    activityResult.data?.getBooleanExtra("soldOn3LastMonth", false)!!
                val min3photos = activityResult.data?.getBooleanExtra("min3photos", false)!!
                val schools = activityResult.data?.getBooleanExtra("schools", false)!!
                val shops = activityResult.data?.getBooleanExtra("shops", false)!!









                realEstateViewModel.getPropertyBySearch(type,city,minSurface,maxSurface,minPrice,maxPrice,onTheMarketLessALastWeek,soldOn3LastMonth,min3photos,schools,shops).observeForever {
                    realEstatesFilter = it
                }

            }
        }











    if (!isExpanded) {


        Scaffold(
            modifier = Modifier.padding(innerPadding),
            topBar = {
                TopBar(
                    title = "RealEstateManager",
                    backNavigate = false,
                    filterScreen = true,
                    drawerButton = true,
                    navigateToFilterScreen = {
                        launcherActivityResult.launch(
                            Intent(
                                context,
                                FilterActivity::class.java
                            ))
                    },
                    navigateToBack = { /*TODO*/ },
                    modifier = Modifier,
                    openDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            },
            content = {

                Log.e("realEstatesResponse", "Success")

                val isRefreshing by realEstateViewModel.isRefreshing.collectAsState()



                SwipeRefresh(
                    modifier = Modifier.padding(it),
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = {
                        realEstateViewModel.refreshRealEstates()
                    },
                ) {


                    LazyColumn {
                        if (!filterState) {

                            items(realEstates) { item ->
                                RowList(
                                    item,
                                    navController,
                                    isExpanded,
                                    realEstateViewModel
                                )
                            }


                        } else {


                            items(realEstatesFilter) { item ->
                                RowList(
                                    item,
                                    navController,
                                    isExpanded,
                                    realEstateViewModel
                                )
                            }

                        }

                    }
                }

            }
        )
    }else {
        Scaffold(
            modifier = Modifier.padding(innerPadding),
            topBar = {
                TopBar(
                    title = "RealEstateManager",
                    backNavigate = false,
                    filterScreen = true,
                    drawerButton = false,
                    navigateToFilterScreen = {
                        launcherActivityResult.launch(
                        Intent(
                            context,
                            FilterActivity::class.java
                        ))
                                             },
                    navigateToBack = { /*TODO*/ },
                    modifier = Modifier,
                    openDrawer = {
                        scope.launch { drawerState.open() }
                    }
                )
            },
            content = {

                Log.e("realEstatesResponse", "Success")

                val isRefreshing by realEstateViewModel.isRefreshing.collectAsState()



                SwipeRefresh(
                    modifier = Modifier.padding(it),
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = {
                        realEstateViewModel.refreshRealEstates()
                    },
                ) {


                    LazyColumn {
                        if (!filterState) {

                            items(realEstates) { item ->
                                RowList(
                                    item,
                                    navController,
                                    isExpanded,
                                    realEstateViewModel
                                )
                            }


                        } else {


                            items(realEstatesFilter) { item ->
                                RowList(
                                    item,
                                    navController,
                                    isExpanded,
                                    realEstateViewModel,
                                )
                            }

                        }

                    }
                }

            }
        )
    }
}




