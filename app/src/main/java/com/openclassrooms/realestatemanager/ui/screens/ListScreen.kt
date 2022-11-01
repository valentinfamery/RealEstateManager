package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.app.Activity.RESULT_OK
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.Response
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
    val refreshing by remember { mutableStateOf(false) }
    val launcherActivityResult = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { activityResult: ActivityResult ->
        if(activityResult.resultCode == RESULT_OK){
            Toast.makeText(context,"Ajout Réussi",Toast.LENGTH_SHORT).show()
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
                        when(val realEstatesResponse = realEstateViewModel.realEstatesResponse){
                            is Response.Empty ->{Log.e("realEstatesResponse", "Empty")}
                            is Response.Loading ->{
                                Log.e("realEstatesResponse", "Loading")
                                Column(modifier = Modifier.fillMaxSize().padding(innerPadding), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                                    CircularProgressIndicator()
                                }
                            }
                            is Response.Success -> {
                                Log.e("realEstatesResponse", "Success")

                                SwipeRefresh(
                                    modifier = Modifier.padding(it),
                                    state = rememberSwipeRefreshState(refreshing),
                                    onRefresh = {

                                    },
                                ) {
                                    LazyColumn {
                                        realEstatesResponse.data.let { response ->
                                            Log.e("items", "listScreen")
                                            Log.e("items", response[0].city.toString())
                                            items(response) { item ->
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

                            }is Response.Failure ->{
                                    Log.e("realEstatesResponse", "Failure")
                            }
                        }
                    }
                )
    }
}




