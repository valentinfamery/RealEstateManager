package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.ScrollableDefaults
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.flowlayout.FlowRow
import com.openclassrooms.realestatemanager.models.RealEstate
import com.openclassrooms.realestatemanager.ui.RealEstateDetail
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


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
    val listState = remember{ realEstateViewModel.getRealEstates }

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
                        RowList(context,item)
                    }


            }
        }
    )
}




