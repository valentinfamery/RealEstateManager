package com.openclassrooms.realestatemanager.ui.screens

import android.content.Intent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.openclassrooms.realestatemanager.ui.NewRealEstateActivity
import com.openclassrooms.realestatemanager.ui.RealEstateDetail
import kotlinx.coroutines.launch

@Composable
@OptIn(ExperimentalFoundationApi::class)
@ExperimentalMaterial3Api
fun MainScreen(navController: NavController, auth: FirebaseAuth) {

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()


    val items = listOf(Icons.Default.Settings)

    val selectedItem = remember { mutableStateOf(items[0]) }




    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {

            ConstraintLayout(modifier = Modifier.fillMaxHeight()) {
                val (userProfilePicture,username,userEmail,drawerItems,buttonLogout) = createRefs()

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.tertiary)
                        .constrainAs(userProfilePicture) {
                            top.linkTo(parent.top, margin = 15.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        }
                )


                Text(text = "Azubal",modifier = Modifier.constrainAs(username) {
                    top.linkTo(userProfilePicture.bottom, margin = 15.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                })
                Text(text = "valentinfamery087@gmail.com",modifier = Modifier.constrainAs(userEmail) {
                    top.linkTo(username.bottom, margin = 5.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                })

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
                        modifier = Modifier
                            .padding(
                                NavigationDrawerItemDefaults.ItemPadding

                            )
                            .constrainAs(drawerItems) {
                                top.linkTo(userEmail.bottom, margin = 15.dp)
                            }
                    )
                }



                Button(
                    onClick = {

                        auth.signOut()
                        navController.navigate("signInScreen")
                    },
                    modifier = Modifier.constrainAs(buttonLogout) {
                        bottom.linkTo(parent.bottom, margin = 20.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    },
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Localized description",
                        modifier = Modifier.size(ButtonDefaults.IconSize)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text("Logout")
                }

            }

        },
        content = {
            Scaffold(
                content = {
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
                },




                floatingActionButton = {
                    val context = LocalContext.current

                    FloatingActionButton(
                        onClick = { /* do something */
                            context.startActivity(
                                Intent(context,
                                    NewRealEstateActivity::class.java)
                            )
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
private fun RowImage() {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .size(84.dp)
            .clip(RoundedCornerShape(corner = CornerSize(16.dp)))
            .background(MaterialTheme.colorScheme.tertiary)

    )
}