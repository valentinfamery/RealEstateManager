package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.openclassrooms.realestatemanager.ui.components.TopBar
import com.openclassrooms.realestatemanager.utils.Screen

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
@ExperimentalMaterial3Api
fun SettingsScreen(navigateToBack : () -> Unit) {
    Scaffold(
        content = {
            Column() {

                TopBar(
                    title = Screen.SettingsScreen.title,
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