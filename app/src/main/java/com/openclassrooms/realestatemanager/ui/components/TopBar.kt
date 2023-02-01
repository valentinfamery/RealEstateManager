package com.openclassrooms.realestatemanager.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.openclassrooms.realestatemanager.R
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(title : String,backNavigate : Boolean,filterScreen : Boolean , drawerButton : Boolean , navigateToFilterScreen : () -> Unit,navigateToBack : () -> Unit,openDrawer : () -> Unit,modifier: Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text(text = title)
        },
        navigationIcon = {
            if(backNavigate) {
                IconButton(onClick = {
                    navigateToBack()
                }) {
                    Icon(Icons.Filled.ArrowBack, "")
                }
            }
            if(drawerButton){
                IconButton(onClick = {
                    openDrawer()
                }) {
                    Icon(Icons.Filled.Menu, "")
                }
            }
        },
        actions = {
            if (filterScreen) {
                IconButton(onClick = {
                    navigateToFilterScreen()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_baseline_filter_list_24),
                        contentDescription = ""
                    )
                }
            }
        },
        modifier = modifier
    )
}