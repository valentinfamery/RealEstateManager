package com.openclassrooms.realestatemanager.ui

import android.annotation.SuppressLint
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.domain.models.RealEstate
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import com.openclassrooms.realestatemanager.ui.screens.*
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme
import com.openclassrooms.realestatemanager.utils.ConnectionReceiver
import com.openclassrooms.realestatemanager.utils.WindowType
import com.openclassrooms.realestatemanager.utils.rememberWindowSizeComposable
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var startScreen : String
    var receiver: ConnectionReceiver? = null
    val filter = IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")

    private val realEstateViewModel: RealEstateViewModel by viewModels()

    @SuppressLint("UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        Log.e("AAAAA","3AA")
        receiver = ConnectionReceiver(realEstateViewModel)
        registerReceiver(receiver,filter)


        setContent {
            Projet_9_OC_RealEstateManagerTheme {
                val userViewModel: UserViewModel = hiltViewModel()






                        val currentUser = auth.currentUser

                        startScreen = if (currentUser == null) {
                            "signInScreen"
                        } else {
                            "mainScreen"
                        }



                val windowSize = rememberWindowSizeComposable()





                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = startScreen) {
                        composable("mainScreen") {
                            MainScreen(
                                navControllerDrawer = navController,
                                auth = auth,
                                userViewModel,
                                realEstateViewModel,
                                windowSize
                            )
                        }
                        composable("settingsScreen") { SettingsScreen(navController = navController) }
                        composable("registerScreen") {
                            RegisterScreen(
                                navController = navController,
                                userViewModel = userViewModel
                            )
                        }
                        composable("signInScreen") { SignInScreen(
                            navigateToMainScreen = {
                                navController.navigate("mainScreen")
                        }, navigateToRegisterScreen = {
                                navController.navigate("registerScreen")
                        }) }
                        composable("editScreen/{item}",
                            arguments = listOf(
                                navArgument("item") {
                                    type = RealEstate.NavigationType
                                }
                            )
                        ) { backStackEntry ->

                            val item = backStackEntry.arguments?.getParcelable<RealEstate>("item")

                            EditScreenRealEstate(
                                realEstateViewModel,
                                item,
                                navController
                            )
                        }


                        if(windowSize.width != WindowType.Expanded) {
                            composable(
                                route = "detailScreen"
                            ) {

                                RealEstateDetailScreen(
                                    realEstateViewModel,
                                    navController,
                                    windowSize
                                )
                            }

                            composable(
                                route = "PictureDetail/{photo_url}",
                                arguments = listOf(navArgument("photo_url") {
                                    type = NavType.StringType
                                })
                            ) { backStackEntry ->
                                val photo_url = backStackEntry.arguments?.getString("photo_url")
                                PictureDetail(photo_url, navController)
                            }
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




























