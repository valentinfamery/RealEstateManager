package com.openclassrooms.realestatemanager.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.ui.screens.MainScreen
import com.openclassrooms.realestatemanager.ui.screens.RegisterScreen
import com.openclassrooms.realestatemanager.ui.screens.SettingsScreen
import com.openclassrooms.realestatemanager.ui.screens.SignInScreen

import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme
import com.openclassrooms.realestatemanager.utils.Resource
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel
import kotlinx.coroutines.launch


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
                        composable("mainScreen") { MainScreen(navController = navController, auth = auth) }
                        composable("settingsScreen") { SettingsScreen(navController = navController) }
                        composable("registerScreen") { RegisterScreen(navController = navController,userViewModel = userViewModel) }
                        composable("signInScreen") { SignInScreen(navController = navController,userViewModel = userViewModel) }
                    }
                }else{
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "signInScreen") {
                        composable("mainScreen") { MainScreen(navController = navController,auth) }
                        composable("settingsScreen") { SettingsScreen(navController = navController) }
                        composable("registerScreen") { RegisterScreen(navController = navController,userViewModel = userViewModel) }
                        composable("signInScreen") { SignInScreen(navController = navController,userViewModel = userViewModel) }
                    }
                }
            }
        }
    }
}




























