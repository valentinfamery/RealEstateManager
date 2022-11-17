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
import androidx.navigation.NavHostController
import androidx.sqlite.db.SimpleSQLiteQuery
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.ui.FilterActivity
import com.openclassrooms.realestatemanager.utils.WindowSize
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
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


    val realEstates by realEstateViewModel.realEstates.collectAsState()
    var filterState by remember {
        mutableStateOf(false)
    }


    var realEstatesFilter by remember {
        mutableStateOf(listOf<RealEstateDatabase>())
    }

    val launcherActivityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {activityResult->
        if(activityResult.resultCode == 0){
           filterState = activityResult.data?.getBooleanExtra("filterState",false)!!
        }
        if(activityResult.resultCode == 1){
            filterState = activityResult.data?.getBooleanExtra("filterState",true)!!
            val type = activityResult.data?.getStringExtra("type")!!
            val city = activityResult.data?.getStringExtra("city")!!



            val query ="SELECT * FROM RealEstateDatabase WHERE('$type' ='' OR typeSQL = '$type')"



            realEstateViewModel.getPropertyBySearch(SimpleSQLiteQuery(query)).observeForever{ list->
                realEstatesFilter = list
            }






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
                                            if(!filterState) {

                                                        items(realEstates) { item ->
                                                            RowList(
                                                                item,
                                                                realEstateViewModel,
                                                                navControllerDrawer,
                                                                windowSize,
                                                                navControllerTwoPane
                                                            )
                                                        }


                                            }else{







                                                        items(realEstatesFilter) { item ->
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
    }
}




