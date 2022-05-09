package com.openclassrooms.realestatemanager.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape

import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass


import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalOf
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.openclassrooms.realestatemanager.R

import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {




    @OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Projet_9_OC_RealEstateManagerTheme(
            ) {

                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "mainScreen") {
                    composable("mainScreen") { MainScreen(navController = navController) }
                    composable("settingsScreen") { SettingsScreen(navController = navController) }
                }

                // A surface container using the 'background' color from the theme

            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
fun MainScreen(navController: NavController) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val items = listOf(Icons.Default.Settings)

    val selectedItem = remember { mutableStateOf(items[0]) }




    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {


            Column (horizontalAlignment = Alignment.CenterHorizontally,){
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary)

                )
                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(15.dp))

                Text(text = "Azubal")
                Text(text = "valentinfamery087@gmail.com")


                Spacer(modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp))
                
                items.forEach { item ->
                    NavigationDrawerItem(
                        icon = { Icon(item, contentDescription = null) },
                        label = { Text("Settings") },
                        selected = item == selectedItem.value,
                        onClick = {
                            scope.launch { drawerState.close() }
                            selectedItem.value = item
                            navController.navigate("settingsScreen") {
                            }
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
                
            }
            ConstraintLayout() {


            }
            
            
        },
        content = {
             Scaffold(
                topBar = {
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
                                //Icon(Icons.Filled.Search, contentDescription = "Localized description")
                            }
                        },
                    )




               },

                content = {
                    val context = LocalContext.current

                    Column(

                    ) {
                        LazyColumn {
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
                                            Text(text = "VIEW DETAIL", style = typography.headlineLarge)
                                            Text(text = "VIEW DETAIL", style = typography.headlineSmall)
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
                                            Text(text = "VIEW DETAIL", style = typography.headlineLarge)
                                            Text(text = "VIEW DETAIL", style = typography.headlineSmall)
                                        }
                                    }
                                }
                            }
                        }


                    }

                          },


                    

                floatingActionButton = {
                    val context = LocalContext.current

                    FloatingActionButton(
                        onClick = { /* do something */
                            context.startActivity(Intent(context,NewRealEstateActivity::class.java))
                                  },
                        modifier = Modifier.clip(RoundedCornerShape(15.dp)),
                    ) {
                        Icon(Icons.Filled.Add, "Localized description")
                    }
                },
            )
        },
    )
}

@Composable
@ExperimentalMaterial3Api
fun SettingsScreen(navController: NavController) {

    Scaffold(


        topBar = {
            CenterAlignedTopAppBar(

                title = {
                    Text(text = "Settings")
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.navigate("mainScreen") {

                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, "")
                   }
                },

            )


       },


        content = {
                  Text(text = "aaaaaa")

        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                modifier = Modifier.clip(RoundedCornerShape(15.dp)),
            ) {
                Icon(Icons.Filled.Favorite, "Localized description")
            }
        }


    )








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










