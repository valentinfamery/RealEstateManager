package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.ui.RealEstateDetail
import com.openclassrooms.realestatemanager.viewmodels.RealEstateViewModel
import com.skydoves.landscapist.glide.GlideImage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(drawerState: DrawerState, scope: CoroutineScope) {

            val context = LocalContext.current
            val listRealEstate = RealEstateViewModel().getRealEstates


            val listRealEstateState = mutableStateListOf(listRealEstate.observeAsState())

            ConstraintLayout() {

                val (centerAlignedTopAppBar,lazyColumn) = createRefs()

                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Real Estate Manager")
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

                LazyColumn(
                    modifier = Modifier.constrainAs(lazyColumn) {
                        top.linkTo(centerAlignedTopAppBar.bottom, margin = 0.dp)
                        start.linkTo(parent.start, margin = 50.dp)
                        end.linkTo(parent.end, margin = 50.dp)
                    }
                ) {
                    itemsIndexed(listRealEstateState) { index,item ->
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(corner = CornerSize(16.dp))
                        ) {
                            Row (Modifier.clickable {
                                val intent = Intent(context, RealEstateDetail::class.java)
                                context.startActivity(intent)

                            }){
                                RowImage(item.value?.get(index)?.getListPicture()?.get(0))
                                Column(modifier = Modifier
                                    .padding(16.dp)
                                    .fillMaxWidth()
                                    .align(Alignment.CenterVertically)
                                ) {
                                    Text(text = item.value?.get(index)?.getType().toString(), style = MaterialTheme.typography.headlineLarge)
                                    Text(text = item.value?.get(index)?.getPrice().toString(), style = MaterialTheme.typography.headlineSmall)
                                }
                            }
                        }

                    }

                }
            }
}



@Composable
private fun RowImage(url: String?) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(84.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
            .background(MaterialTheme.colorScheme.tertiary)

    ){
        GlideImage(
            imageModel = url,
            // Crop, Fit, Inside, FillHeight, FillWidth, None
            contentScale = ContentScale.Crop,
        )
    }
}