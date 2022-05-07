package com.openclassrooms.realestatemanager.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
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
import androidx.compose.ui.graphics.Color
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
@ExperimentalMaterial3Api
fun MainScreen(navController: NavController) {



    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val items = listOf(Icons.Default.Settings)

    val selectedItem = remember { mutableStateOf(items[0]) }




    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ConstraintLayout {
                // Create references for the composables to constrain
                val (button, text) = createRefs()


                        Box(
                            modifier = Modifier
                                .size(100.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.tertiary)
                                .constrainAs(text) {
                                    top.linkTo(parent.top, 15.dp)
                                    start.linkTo(parent.start, 100.dp)
                                    end.linkTo(parent.end, 100.dp)
                                }

                        )







            }










            Spacer(modifier = Modifier
                .fillMaxWidth()
                .height(15.dp))
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
                    Column(

                    ) {
                        Text(text = "bbbbbbbbbb")
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










