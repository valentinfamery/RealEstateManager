package com.openclassrooms.realestatemanager.ui.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scope: CoroutineScope,
    drawerState: DrawerState,
    title: String,
    navControllerDrawer: NavController
) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = {
                scope.launch { drawerState.open() }
            }) {
                Icon(Icons.Filled.Menu,"")
            }
        },
        actions = {
            IconButton(onClick = {
                navControllerDrawer.navigate("filterScreen")

            }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_filter_list_24 ), contentDescription = "")
            }
        },

        )
}