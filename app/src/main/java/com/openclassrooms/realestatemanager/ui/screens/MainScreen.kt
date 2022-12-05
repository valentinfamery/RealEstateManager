package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.adaptive.FoldAwareConfiguration
import com.google.accompanist.adaptive.HorizontalTwoPaneStrategy
import com.google.accompanist.adaptive.TwoPane
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.ui.NewRealEstateActivity
import com.openclassrooms.realestatemanager.utils.Screen
import com.openclassrooms.realestatemanager.utils.WindowSize
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import com.openclassrooms.realestatemanager.utils.Utils
import com.openclassrooms.realestatemanager.utils.WindowType
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun MainScreen(
    navControllerDrawer: NavController,
    auth: FirebaseAuth,
    userViewModel: UserViewModel,
    realEstateViewModel: RealEstateViewModel,
    windowSize: WindowSize,
    realEstateId: String,
    realEstateIdSet : (realEstateId : String) ->Unit
) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var selectedItem by remember { mutableStateOf(0) }
    val items = listOf(Screen.ListScreen, Screen.MapScreen)

    val navController = rememberNavController()
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
                userViewModel
            )
        },
        content = {

            if (windowSize.width != WindowType.Expanded) {

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
                                    windowSize,
                                    realEstateId,
                                    realEstateIdSet = {
                                        realEstateIdSet(it)
                                    }
                                )
                            }
                            composable(Screen.MapScreen.route) {
                                MapScreen(
                                    drawerState,
                                    scope,
                                    realEstateViewModel,
                                    navControllerDrawer,
                                    windowSize
                                )
                            }
                        }

                    },
                    bottomBar = {
                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = { Icon(item.icon, contentDescription = null) },
                                    label = { Text(item.title) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        navController.navigate(item.route)
                                    }
                                )
                            }
                        }
                    },
                    floatingActionButton = {


                                FloatingActionButton(
                                    onClick = {
                                        val isInternetAvailable = Utils.isInternetAvailable(context)

                                        if(isInternetAvailable){
                                            context.startActivity(
                                                Intent(context, NewRealEstateActivity::class.java)
                                            )
                                        }else{
                                            Toast.makeText(context,"Impossible il n'y a pas de connexion Internet",Toast.LENGTH_LONG).show()
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



                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (navigationRail,box) = createRefs()

                    NavigationRail(modifier = Modifier
                        .constrainAs(navigationRail) {
                            top.linkTo(parent.top, margin = 0.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            bottom.linkTo(parent.bottom, margin = 0.dp)
                        }
                        .fillMaxHeight()
                        .fillMaxWidth(0.05f)) {
                        NavigationRailItem(
                            icon = { Icon(Icons.Default.Menu, contentDescription = null) },
                            selected = selectedItem == 3,
                            onClick = {
                                scope.launch { drawerState.open() }
                            }
                        )

                        items.forEachIndexed { index, item ->
                            NavigationRailItem(
                                icon = { Icon(item.icon, contentDescription = null) },
                                label = { Text(item.title) },
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    navController.navigate(item.route)
                                }
                            )
                        }
                    }


                    Box(modifier = Modifier
                        .constrainAs(box) {
                            top.linkTo(parent.top, margin = 0.dp)
                            start.linkTo(navigationRail.end, margin = 0.dp)
                            bottom.linkTo(parent.bottom, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }
                        .fillMaxHeight()
                        .fillMaxWidth(0.95f)
                    ) {
                        TwoPane(
                            first = {
                                Scaffold(
                                    modifier = Modifier.background(color = Color.Red),
                                    content = {innerPadding->


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
                                                    windowSize,
                                                    realEstateId,
                                                    realEstateIdSet = {
                                                        realEstateIdSet(it)
                                                    }
                                                )
                                            }
                                            composable(Screen.MapScreen.route) {
                                                MapScreen(
                                                    drawerState,
                                                    scope,
                                                    realEstateViewModel,
                                                    navControllerDrawer,
                                                    windowSize
                                                )
                                            }
                                        }

                                    },
                                    floatingActionButton = {
                                        FloatingActionButton(
                                            onClick = { /* do something */
                                                context.startActivity(
                                                    Intent(context, NewRealEstateActivity::class.java)
                                                )
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
                                var navControllerTwoPane = rememberNavController()

                                NavHost(navController = navControllerTwoPane , startDestination = "detailScreen"){
                                    composable("detailScreen") {
                                        if (realEstateId != "") {

                                            val itemRealEstate by realEstateViewModel.realEstateById(realEstateId).observeAsState()

                                            RealEstateDetailScreen(
                                                realEstateViewModel = realEstateViewModel,
                                                navController = navController,
                                                windowSize = windowSize,
                                                itemRealEstate
                                            )

                                        }
                                    }
                                }



                            },
                            strategy = HorizontalTwoPaneStrategy(splitFraction = 0.475f),
                            displayFeatures =  calculateDisplayFeatures(activity = context as Activity),
                            foldAwareConfiguration = FoldAwareConfiguration.AllFolds,
                        )
                    }






                }





            }

        },

        )





}


