package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.presentation.viewModels.RealEstateViewModel
import com.openclassrooms.realestatemanager.ui.components.TopBar

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun SettingsScreen(navigateToBack : () -> Unit) {
    Scaffold(
        content = {
            Column() {

                TopBar(
                    title = "Settings",
                    backNavigate = true,
                    filterScreen = false,
                    drawerButton = false,
                    navigateToFilterScreen = { /*TODO*/ },
                    navigateToBack = {navigateToBack()},
                    openDrawer = { /*TODO*/ },
                    modifier = Modifier
                )

            }
        },
    )
}