package com.openclassrooms.realestatemanager.ui.screens

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.ui.RealEstateDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(drawerState: DrawerState, scope: CoroutineScope) {
    ConstraintLayout() {

        val (centerAlignedTopAppBar) = createRefs()

        CenterAlignedTopAppBar(
            title = {
                Text(text = "Map")
            },
            navigationIcon = {
                IconButton(onClick = {
                    scope.launch { drawerState.open() }
                }) {
                    Icon(Icons.Filled.Menu,"")
                }
            },
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(Icons.Filled.Search, contentDescription = "Localized description")
                }
            },
            modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                top.linkTo(parent.top, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        )
    }

}