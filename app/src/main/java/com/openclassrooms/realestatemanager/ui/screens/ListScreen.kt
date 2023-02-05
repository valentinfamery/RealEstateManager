package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.openclassrooms.realestatemanager.domain.models.Estate
import com.openclassrooms.realestatemanager.presentation.viewModels.EstateViewModel
import com.openclassrooms.realestatemanager.ui.components.TopBar
import com.openclassrooms.realestatemanager.utils.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@SuppressLint("UnrememberedMutableState", "SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    estateViewModel: EstateViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
    isExpanded: Boolean
) {

    val realEstates by estateViewModel.realEstates.collectAsState()
    var dialogState by remember {
        mutableStateOf(false)
    }
    var filterState by remember { mutableStateOf(false) }
    var realEstatesFilter by remember { mutableStateOf(listOf<Estate>()) }


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
        estateViewModel.getPropertyBySearch(type,city,minSurface,maxSurface,minPrice,maxPrice,onTheMarketLessALastWeek,soldOn3LastMonth,min3photos,schools,shops).observeForever {
            realEstatesFilter = it
        }
    }

    if (!isExpanded) {


        Scaffold(
            modifier = Modifier.padding(innerPadding),
            topBar = {
                TopBar(
                    title = Screen.ListScreen.title,
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

                val isRefreshing by estateViewModel.isRefreshing.collectAsState()



                SwipeRefresh(
                    modifier = Modifier.padding(it),
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = {
                        estateViewModel.refreshRealEstates()
                    },
                ) {


                    LazyColumn {
                        if (!filterState) {

                            items(realEstates) { item ->
                                RowList(
                                    item,
                                    navController,
                                    isExpanded,
                                    estateViewModel
                                )
                            }


                        } else {


                            items(realEstatesFilter) { item ->
                                RowList(
                                    item,
                                    navController,
                                    isExpanded,
                                    estateViewModel
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
                    title = Screen.ListScreen.title,
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

                val isRefreshing by estateViewModel.isRefreshing.collectAsState()



                SwipeRefresh(
                    modifier = Modifier.padding(it),
                    state = rememberSwipeRefreshState(isRefreshing),
                    onRefresh = {
                        estateViewModel.refreshRealEstates()
                    },
                ) {


                    LazyColumn {
                        if (!filterState) {

                            items(realEstates) { item ->
                                RowList(
                                    item,
                                    navController,
                                    isExpanded,
                                    estateViewModel
                                )
                            }


                        } else {


                            items(realEstatesFilter) { item ->
                                RowList(
                                    item,
                                    navController,
                                    isExpanded,
                                    estateViewModel,
                                )
                            }

                        }

                    }
                }

            }
        )
    }
}




