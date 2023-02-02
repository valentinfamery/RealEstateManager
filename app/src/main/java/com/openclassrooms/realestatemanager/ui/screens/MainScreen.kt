package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu

import androidx.compose.material3.*

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.accompanist.adaptive.FoldAwareConfiguration
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.domain.models.RealEstateDatabase
import com.openclassrooms.realestatemanager.utils.Screen
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun MainScreen(
    navControllerDrawer: NavController,
    auth: FirebaseAuth,
    userViewModel: UserViewModel,
    realEstateViewModel: RealEstateViewModel,
    isExpanded: Boolean,
    navigateToNewScreen : () -> Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val items = listOf(Screen.ListScreen, Screen.MapScreen)

    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val context = LocalContext.current

    ModalNavigationDrawer(
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            DrawerScreen(
                drawerState,
                scope,
                navControllerDrawer,
                auth,
                userViewModel,
                modifier = Modifier.fillMaxSize()
            )
        },
        content = {

            if (!isExpanded) {

                Scaffold(
                    content = { innerPadding ->

                        NavHost(
                            navController = navController,
                            startDestination = "listScreen"
                        ) {
                            composable(Screen.ListScreen.route) {
                                ListScreen(
                                    drawerState,
                                    scope,
                                    realEstateViewModel,
                                    innerPadding,
                                    navControllerDrawer,
                                    isExpanded
                                )
                            }
                            composable(Screen.MapScreen.route) {
                                MapScreen(
                                    drawerState,
                                    scope,
                                    realEstateViewModel,
                                    navControllerDrawer,
                                    isExpanded
                                )
                            }
                        }

                    },
                    bottomBar = {
                        NavigationBar {
                            items.forEach { item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(item.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                    onClick = {
                                        navController.navigate(item.route)
                                    }
                                )
                            }
                        }
                    },
                    floatingActionButton = {


                                FloatingActionButton(
                                    onClick = {
                                        val isInternetAvailable = Utils.isInternetAvailable()

                                        if(isInternetAvailable){
                                            navigateToNewScreen()
                                        }else{
                                            Toast.makeText(context,"Impossible there is no internet connection",Toast.LENGTH_LONG).show()
                                        }


                                    },
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(15.dp))
                                ) {
                                    Icon(Icons.Filled.Add, "Localized description")
                                }


                    }

                )

            } else {



                    Row {


                        NavigationRail {
                            NavigationRailItem(
                                icon = { Icon(Icons.Default.Menu, contentDescription = null) },
                                selected = false,
                                onClick = {
                                    scope.launch { drawerState.open() }
                                }
                            )

                            items.forEach { item ->
                                NavigationRailItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(item.title) },
                                    selected = currentDestination?.hierarchy?.any { it.route == item.route } == true,
                                    onClick = {

                                        navController.navigate(item.route)
                                    }
                                )
                            }
                        }


                        Box {
                            TwoPane(
                                first = {
                                    Scaffold(
                                        modifier = Modifier.background(color = Color.Red),
                                        content = { innerPadding ->


                                            NavHost(
                                                navController = navController,
                                                startDestination = "listScreen"
                                            ) {
                                                composable(Screen.ListScreen.route) {
                                                    ListScreen(
                                                        drawerState,
                                                        scope,
                                                        realEstateViewModel,
                                                        innerPadding,
                                                        navControllerDrawer,
                                                        isExpanded
                                                    )
                                                }
                                                composable(Screen.MapScreen.route) {
                                                    MapScreen(
                                                        drawerState,
                                                        scope,
                                                        realEstateViewModel,
                                                        navControllerDrawer,
                                                        isExpanded
                                                    )
                                                }
                                            }

                                        },
                                        floatingActionButton = {
                                            FloatingActionButton(
                                                onClick = { /* do something */
                                                    navigateToNewScreen()
                                                },
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(15.dp))
                                            ) {
                                                Icon(Icons.Filled.Add, "Localized description")
                                            }
                                        }

                                    )
                                },
                                second = {
                                    val navControllerTwoPane = rememberNavController()

                                    val id = realEstateViewModel.realEstateIdDetail.collectAsState()

                                    NavHost(
                                        navController = navControllerTwoPane,
                                        startDestination = "detailScreen"
                                    ) {
                                        composable("detailScreen") {
                                            if (id.value != "") {

                                                val itemRealEstate by realEstateViewModel.realEstateById(
                                                    id.value
                                                ).observeAsState()

                                                RealEstateDetailScreen(
                                                    navigateToEditScreen = {

                                                    },
                                                    navigateToEditScreenExpanded = {
                                                        val item = Uri.encode(Gson().toJson(itemRealEstate))
                                                        navControllerTwoPane.navigate("editScreen/$item")
                                                    },
                                                    isExpanded,
                                                    itemRealEstate,
                                                    navigateToBack = {

                                                    },
                                                    modifier = Modifier.fillMaxSize()
                                                )

                                            }
                                        }

                                        composable("editScreen/{item}",
                                            arguments = listOf(
                                                navArgument("item") {
                                                    type = RealEstateDatabase
                                                }
                                            )
                                        ) { backStackEntry ->

                                            val item = backStackEntry.arguments?.getParcelable<RealEstateDatabase>("item")

                                            realEstateViewModel.fillMyUiState(item?.listPhotoWithText!!)


                                            EditScreenRealEstate(
                                                realEstateViewModel,
                                                item,
                                                navControllerTwoPane,
                                                setPhotoUrl = {

                                                },

                                                )
                                        }
                                    }


                                },
                                strategy = HorizontalTwoPaneStrategy(splitFraction = 0.475f),
                                displayFeatures = calculateDisplayFeatures(activity = context as Activity),
                                foldAwareConfiguration = FoldAwareConfiguration.AllFolds,
                            )
                        }


                    }







            }

        },

        )





}


