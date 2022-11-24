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
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.ui.FilterActivity
import com.openclassrooms.realestatemanager.utils.WindowSize
import com.openclassrooms.realestatemanager.utils.WindowType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter
import java.util.*


@SuppressLint("UnrememberedMutableState", "SimpleDateFormat")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    realEstateViewModel: RealEstateViewModel,
    innerPadding: PaddingValues,
    navController: NavController,
    windowSize: WindowSize,
    realEstateId: String,
    realEstateIdSet : (realEstateId : String) ->Unit
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

                val c = Calendar.getInstance()
                val fmt: DateTimeFormatter = DateTimeFormat.forPattern("dd/MM/yyyy")

                val dateToday = DateTime(c.time)
                val dateTodayFinal = dateToday.toString(fmt)

                val dateMinusThreeMonth = dateToday.minusMonths(3).toString(fmt)
                val dateMinus1Week = dateToday.minusDays(7).toString(fmt)

                val query = """SELECT * FROM RealEstateDatabase WHERE 
                        ('$type' ='' OR type LIKE '%$type%' ) AND 
                        ('$city' ='' OR city LIKE '%$city%' ) AND
                        ($schools = false OR schoolsNear = $schools ) AND 
                        ($shops = false OR shopsNear = $shops ) AND 
                        ($min3photos = false OR count_photo >= 3 ) AND
                        ($minSurface =0 AND $maxSurface = 0  OR  area BETWEEN $minSurface AND $maxSurface  ) AND 
                        ($minPrice =0 AND $maxPrice = 0  OR  price BETWEEN $minPrice AND $maxPrice ) AND 
                        ($onTheMarketLessALastWeek = false  OR  dateOfEntry BETWEEN '$dateMinus1Week' AND '$dateTodayFinal' ) AND 
                        ($soldOn3LastMonth = false  OR  dateOfSale <> '00/00/0000' OR  dateOfSale BETWEEN '$dateMinusThreeMonth' AND '$dateTodayFinal' ) """





                Log.e("query", query)

                realEstateViewModel.getPropertyBySearch(SimpleSQLiteQuery(query))
                    .observeForever { list ->
                        realEstatesFilter = list
                    }
            }
        }











    if (windowSize.width == WindowType.Compact) {


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
            content = { it ->

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
                                    windowSize,
                                    realEstateId,
                                    realEstateIdSet = {
                                        realEstateIdSet(it)
                                    }
                                )
                            }


                        } else {


                            items(realEstatesFilter) { item ->
                                RowList(
                                    item,
                                    navController,
                                    windowSize,
                                    realEstateId,
                                    realEstateIdSet = {
                                        realEstateIdSet(it)
                                    }
                                )
                            }

                        }

                    }
                }

            }
        )
    }else if (windowSize.width == WindowType.Expanded){
        Scaffold(
            modifier = Modifier.padding(innerPadding),
            topBar = {

                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "RealEstateManager")
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
            content = { it ->

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
                                    windowSize,
                                    realEstateId,
                                    realEstateIdSet = {
                                        realEstateIdSet(it)
                                    }
                                )
                            }


                        } else {


                            items(realEstatesFilter) { item ->
                                RowList(
                                    item,
                                    navController,
                                    windowSize,
                                    realEstateId,
                                    realEstateIdSet = {
                                        realEstateIdSet(it)
                                    }
                                )
                            }

                        }

                    }
                }

            }
        )
    }
}




