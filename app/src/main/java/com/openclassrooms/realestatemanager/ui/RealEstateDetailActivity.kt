package com.openclassrooms.realestatemanager.ui

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme

class RealEstateDetail : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projet_9_OC_RealEstateManagerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting2()
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun Greeting2() {

    Scaffold(
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { /* do something */ },
                icon = { Icon(Icons.Filled.Edit, "Localized description") },
                text = { Text(text = "Edit") },
                modifier = Modifier.clip(RoundedCornerShape(15.dp))
            )
        },
        content = {

            ConstraintLayout() {

                val (centerAlignedTopAppBar, lazyVerticalGrid) = createRefs()

                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Estate Manager")
                    },
                    navigationIcon = {
                        val activity = (LocalContext.current as? Activity)
                        IconButton(onClick = {
                            activity?.finish()
                        }) {
                            Icon(Icons.Filled.ArrowBack, "")
                        }
                    },
                    modifier = Modifier.constrainAs(centerAlignedTopAppBar) {
                        top.linkTo(parent.top, margin = 0.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }

                )

                LazyVerticalGrid(
                    modifier = Modifier.constrainAs(lazyVerticalGrid) {
                        top.linkTo(centerAlignedTopAppBar.bottom, margin = 10.dp)
                    },
                    columns = GridCells.Adaptive(minSize = 150.dp)
                ) {
                    item {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(corner = CornerSize(16.dp))
                        ) {
                            Row {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(
                                        text = "VIEW DETAIL",
                                        style = MaterialTheme.typography.headlineLarge
                                    )
                                    Text(
                                        text = "VIEW DETAIL",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(corner = CornerSize(16.dp))
                        ) {
                            Row {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(
                                        text = "VIEW DETAIL",
                                        style = MaterialTheme.typography.headlineLarge
                                    )
                                    Text(
                                        text = "VIEW DETAIL",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }
                    }
                    item {
                        Card(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(corner = CornerSize(16.dp))
                        ) {
                            Row {
                                Column(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth()
                                        .align(Alignment.CenterVertically)
                                ) {
                                    Text(
                                        text = "VIEW DETAIL",
                                        style = MaterialTheme.typography.headlineLarge
                                    )
                                    Text(
                                        text = "VIEW DETAIL",
                                        style = MaterialTheme.typography.headlineSmall
                                    )
                                }
                            }
                        }
                    }
                }
            }

        })





}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    Projet_9_OC_RealEstateManagerTheme {
        Greeting2()
    }
}