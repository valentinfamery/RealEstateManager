package com.openclassrooms.realestatemanager.ui

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.openclassrooms.realestatemanager.domain.models.Estate
import com.openclassrooms.realestatemanager.presentation.viewModels.EstateViewModel
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import com.openclassrooms.realestatemanager.ui.screens.*
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme

import com.openclassrooms.realestatemanager.utils.ConnectionReceiver
import com.openclassrooms.realestatemanager.utils.Screen
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private var receiver: ConnectionReceiver? = null
    private val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")

    private val estateViewModel: EstateViewModel by viewModels()

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        auth = Firebase.auth

        receiver = ConnectionReceiver(estateViewModel)
        registerReceiver(receiver,filter)

        setContent {
            Projet_9_OC_RealEstateManagerTheme {
                val userViewModel: UserViewModel = hiltViewModel()

                val currentUser = auth.currentUser

                val widthSizeClass = calculateWindowSizeClass(this).widthSizeClass

                val isExpanded = widthSizeClass == WindowWidthSizeClass.Expanded

                val id = estateViewModel.realEstateIdDetail.collectAsState()

                val navControllerMainActivity = rememberNavController()


                    NavHost(navController = navControllerMainActivity, startDestination = if (currentUser == null)
                        Screen.SignInScreen.route
                    else
                        Screen.MainScreen.route) {

                        composable(Screen.MainScreen.route) {
                            MainScreen(
                                navControllerDrawer = navControllerMainActivity,
                                auth = auth,
                                userViewModel,
                                estateViewModel,
                                isExpanded,
                                navigateToNewScreen = {
                                    navControllerMainActivity.navigate(Screen.NewScreen.route)
                                }
                            )
                        }
                        composable(Screen.SettingsScreen.route) { SettingsScreen(navigateToBack = {navControllerMainActivity.popBackStack()}) }
                        composable(Screen.RegisterScreen.route) {
                            RegisterScreen(
                                isExpanded,
                                navController = navControllerMainActivity,
                                userViewModel = userViewModel
                            )
                        }
                        composable(Screen.SignInScreen.route) {
                            SignInScreen(
                                isExpanded,
                                navigateToMainScreen = {
                                    navControllerMainActivity.navigate(Screen.MainScreen.route)
                                }) {
                                navControllerMainActivity.navigate(Screen.RegisterScreen.route)
                            }
                        }

                        composable(Screen.NewScreen.route){
                            NewRealEstateScreen(isExpanded,estateViewModel,userViewModel, navigateToBack = {
                                navControllerMainActivity.popBackStack()
                            })
                        }





                            composable(
                                route = Screen.DetailScreen.route
                            ) {

                                if (id.value != "") {

                                    val itemRealEstate by estateViewModel.realEstateById(
                                        id.value
                                    ).observeAsState()

                                    RealEstateDetailScreen(
                                        navigateToEditScreen = {
                                            val item = Uri.encode(Gson().toJson(itemRealEstate))
                                            navControllerMainActivity.navigate("${Screen.EditScreen.route}/$item")
                                        },
                                        navigateToEditScreenExpanded = {

                                        },
                                        isExpanded,
                                        itemRealEstate,
                                        navigateToBack = {
                                            navControllerMainActivity.popBackStack()
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )

                                }


                            }

                            composable("${Screen.EditScreen.route}/{item}",
                                arguments = listOf(
                                    navArgument("item") {
                                        type = Estate.NavigationType
                                    }
                                )
                            ) { backStackEntry ->

                                val item =
                                    backStackEntry.arguments?.getParcelable<Estate>("item")

                                estateViewModel.fillMyUiState(item?.listPhotoWithText!!)


                                EditScreenRealEstate(
                                    estateViewModel,
                                    item,
                                    navControllerMainActivity
                                )
                            }

                        composable(Screen.CurrencyConverterScreen.route){
                            CurrencyConverterScreen(navigateToBack = {navControllerMainActivity.popBackStack()})
                        }


                    }
            }
        }
    }



    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }
}




























