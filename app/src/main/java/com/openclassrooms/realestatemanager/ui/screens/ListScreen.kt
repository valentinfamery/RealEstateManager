package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
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
    var dialogState by remember {
        mutableStateOf(false)
    }
    var filterState by remember { mutableStateOf(false) }
    var realEstatesFilter by remember { mutableStateOf(listOf<RealEstateDatabase>()) }


    var type by remember{ mutableStateOf("") }
    var city by remember{ mutableStateOf("") }
    var minSurface by remember {
        mutableStateOf(0)
    }
    var maxSurface by remember {
        mutableStateOf(0)
    }
    var minPrice by remember {
        mutableStateOf(0)
    }
    var maxPrice by remember {
        mutableStateOf(0)
    }
    var onTheMarketLessALastWeek by remember {
        mutableStateOf(false)
    }
    var soldOn3LastMonth by remember {
        mutableStateOf(false)
    }
    var min3photos by remember {
        mutableStateOf(false)
    }
    var schools by remember {
        mutableStateOf(false)
    }
    var shops by remember {
        mutableStateOf(false)
    }
    
    FilterScreen(dialogState, editFilterProperty = {filterState1, entryType1, entryCity1, entryMinSurface1, entryMaxSurface1, entryMinPrice1, entryMaxPrice1, onTheMarketLessALastWeek1, soldOn3LastMonth1, min3photos1, schools1, shops1 ->
        filterState = filterState1
        type = entryType1
        city = entryCity1
        minSurface = entryMinSurface1
        maxSurface = entryMaxSurface1
        minPrice = entryMinPrice1
        maxPrice = entryMaxPrice1
        onTheMarketLessALastWeek = onTheMarketLessALastWeek1
        soldOn3LastMonth = soldOn3LastMonth1
        min3photos = min3photos1
        schools = schools1
        shops = shops1

    }, closeDialog = {
        dialogState = false
    },
        resetFilter = {
            filterState = false
        }
    )

    if(filterState){
        realEstateViewModel.getPropertyBySearch(type,city,minSurface,maxSurface,minPrice,maxPrice,onTheMarketLessALastWeek,soldOn3LastMonth,min3photos,schools,shops).observeForever {
            realEstatesFilter = it
        }
    }

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
                val onTheMarketLessALastWeek = activityResult.data?.getBooleanExtra("onTheMarketLessALastWeek", false)!!
                val soldOn3LastMonth = activityResult.data?.getBooleanExtra("soldOn3LastMonth", false)!!
                val min3photos = activityResult.data?.getBooleanExtra("min3photos", false)!!
                val schools = activityResult.data?.getBooleanExtra("schools", false)!!
                val shops = activityResult.data?.getBooleanExtra("shops", false)!!

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
                        dialogState = true
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
                        dialogState = true
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




