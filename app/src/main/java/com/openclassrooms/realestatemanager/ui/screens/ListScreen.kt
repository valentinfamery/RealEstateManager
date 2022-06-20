package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import kotlinx.coroutines.CoroutineScope


@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    drawerState: DrawerState,
    scope: CoroutineScope,
    realEstateViewModel: RealEstateViewModel,
    innerPadding: PaddingValues,
) {

    val context = LocalContext.current
    val navController = rememberNavController()
    val textState = remember { mutableStateOf(TextFieldValue(""))}

    val items: List<RealEstate> by realEstateViewModel.getRealEstates.observeAsState(listOf())


    Scaffold(
        modifier = Modifier.padding(innerPadding),
        topBar = {

            NavHost(
                navController = navController,
                startDestination = "topBarMap",
            ) {
                composable("topBarMap") { TopBar(scope,drawerState,navController,"RealEstateManager")}
                composable("SearchView") { SearchView(textState,navController)}
            }

        },
        content = {
            LazyColumn(


                modifier = Modifier.padding(it),
            ){

                    items(items){ item ->
                        RowList(context,item,realEstateViewModel)
                    }


            }
        }
    )
}




