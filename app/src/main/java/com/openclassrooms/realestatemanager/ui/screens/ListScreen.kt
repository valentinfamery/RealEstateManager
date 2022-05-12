package com.openclassrooms.realestatemanager.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.ui.RealEstateDetail
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(drawerState: DrawerState, scope: CoroutineScope) {

            val context = LocalContext.current

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
                    // Add a single item
                    item {

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
                                RowImage()
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(text = "VIEW DETAIL", style = MaterialTheme.typography.headlineLarge)
                                    Text(text = "VIEW DETAIL", style = MaterialTheme.typography.headlineSmall)
                                }
                            }
                        }
                    }

                    // Add another single item
                    item {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(corner = CornerSize(16.dp))
                        ) {
                            Row {
                                RowImage()
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(text = "VIEW DETAIL", style = MaterialTheme.typography.headlineLarge)
                                    Text(text = "VIEW DETAIL", style = MaterialTheme.typography.headlineSmall)
                                }
                            }
                        }
                    }
                }
            }
}

@Composable
private fun RowImage() {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(84.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
            .background(MaterialTheme.colorScheme.tertiary)

    )
}