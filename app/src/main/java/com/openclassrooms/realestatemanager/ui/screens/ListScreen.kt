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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
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


    val listState = remember{ realEstateViewModel.getRealEstates }


    Scaffold(
        modifier = Modifier.padding(innerPadding),
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Real Estate Manager")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        scope.launch { drawerState.open() }
                    }) {
                        Icon(Icons.Filled.Menu, "")
                    }
                },
                actions = {
                    IconButton(onClick = { /*TODO*/ }) {
                        Icon(Icons.Filled.Search, contentDescription = "Localized description")
                    }
                },
            )
        },
        content = {
            LazyColumn(modifier = Modifier.padding(it),
            ){
                items(listState){item ->
                    RowList(context,item)
                }
            }
        }
    )








}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RowList(context: Context, item: RealEstate) {
    Card(
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(corner = CornerSize(16.dp))
    ) {
        Row(Modifier.clickable {
            val intent =
                Intent(context, RealEstateDetail::class.java)
            context.startActivity(intent)

        }) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(84.dp)
                    .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
                    .background(MaterialTheme.colorScheme.tertiary)
            ) {
                GlideImage(
                    imageModel = "",
                    contentScale = ContentScale.Crop,
                )
            }

            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterVertically)
            ) {
                Text(
                    text = item.getType().toString(),
                    style = MaterialTheme.typography.headlineLarge
                )
                Text(
                    text = item.getPrice().toString(),
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}


