package com.openclassrooms.realestatemanager.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

import androidx.compose.material3.*


import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.ui.screens.MainScreen
import com.openclassrooms.realestatemanager.ui.screens.RegisterScreen
import com.openclassrooms.realestatemanager.ui.screens.SettingsScreen
import com.openclassrooms.realestatemanager.ui.screens.SignInScreen

import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel


class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        auth = Firebase.auth
        setContent {
            Projet_9_OC_RealEstateManagerTheme(
            ) {
                val currentUser = auth.currentUser
                if(currentUser != null){
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") { MainScreen(navControllerDrawer = navController, auth = auth,userViewModel) }
                        composable("settingsScreen") { SettingsScreen(navController = navController) }
                        composable("registerScreen") { RegisterScreen(navController = navController,userViewModel = userViewModel) }
                        composable("signInScreen") { SignInScreen(navController = navController,userViewModel = userViewModel) }
                    }
                }else{
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "signInScreen") {
                        composable("mainScreen") { MainScreen(
                            navControllerDrawer = navController,
                            auth,
                            userViewModel
                        ) }
                        composable("settingsScreen") { SettingsScreen(navController = navController) }
                        composable("registerScreen") { RegisterScreen(navController = navController,userViewModel = userViewModel) }
                        composable("signInScreen") { SignInScreen(navController = navController,userViewModel = userViewModel) }
                    }
                }
            }
        }
    }
}




























