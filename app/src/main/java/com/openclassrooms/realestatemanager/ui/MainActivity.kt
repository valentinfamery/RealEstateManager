package com.openclassrooms.realestatemanager.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
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
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable


import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.openclassrooms.realestatemanager.R

import com.openclassrooms.realestatemanager.ui.ui.theme.Projet_9_OC_RealEstateManagerTheme
import com.openclassrooms.realestatemanager.utils.Resource
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {

    private val userViewModel: UserViewModel by viewModels()
    private var isCurrentUserLoggedIn : Boolean = false

    private lateinit var auth: FirebaseAuth

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)


        userViewModel.isCurrentUserLoggedIn.observe(this) { t: Boolean? ->
            if (t != null) {
                isCurrentUserLoggedIn = t
            }else {
                finish();
            }
        }

        auth = Firebase.auth

        setContent {
            Projet_9_OC_RealEstateManagerTheme(
            ) {
                if(isCurrentUserLoggedIn == true){
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "mainScreen") {
                        composable("mainScreen") { MainScreen(navController = navController) }
                        composable("settingsScreen") { SettingsScreen(navController = navController) }
                    }
                }else if (isCurrentUserLoggedIn == false){
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "signInScreen") {
                        composable("mainScreen") { MainScreen(navController = navController) }
                        composable("settingsScreen") { SettingsScreen(navController = navController) }
                        composable("registerScreen") { RegisterScreen(navController = navController,userViewModel = userViewModel) }
                        composable("signInScreen") { SignInScreen(navController = navController,userViewModel = userViewModel) }
                    }
                }
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
                    onClick = { navController.navigate("signInScreen") },
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





        content = {


            ConstraintLayout() {
                val (centerAlignedTopAppBar,text) = createRefs()

                CenterAlignedTopAppBar(

                    title = {
                        Text(text = "Settings")
                    },
                    navigationIcon = {
                        IconButton(onClick = {
                            navController.popBackStack()
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

                Text(text = "aaaaaa",modifier = Modifier.constrainAs(text) {
                    top.linkTo(centerAlignedTopAppBar.bottom, margin = 10.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                })
            }


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



@Composable
private fun RegisterScreen(navController: NavController, userViewModel: UserViewModel){
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (image,centerAlignedTopAppBar,textFieldEmail,textFieldUsername,textFieldPassword,buttonConfirmRegister) = createRefs()

        CenterAlignedTopAppBar(

            title = {
                Text(text = "Register")
            },
            navigationIcon = {
                IconButton(onClick = {
                    navController.popBackStack()
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

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier = Modifier.constrainAs(image){
                top.linkTo(centerAlignedTopAppBar.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        )

        var textEmail by rememberSaveable { mutableStateOf("") }
        var textUsername by rememberSaveable { mutableStateOf("") }
        var textPassword by rememberSaveable { mutableStateOf("") }

        TextField(
            value = textEmail,
            onValueChange = { textEmail = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.constrainAs(textFieldEmail) {
                top.linkTo(image.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }

        )

        TextField(
            value = textUsername,
            onValueChange = { textUsername = it },
            label = { Text("Username") },
            singleLine = true,
            modifier = Modifier.constrainAs(textFieldUsername) {
                top.linkTo(textFieldEmail.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }

        )

        TextField(
            value = textPassword,
            onValueChange = { textPassword = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.constrainAs(textFieldPassword) {
                top.linkTo(textFieldUsername.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }

        )

        val context = LocalContext.current

        Button(

            onClick = {

                userViewModel.createUser(textUsername,textEmail,textPassword).observeForever {
                    when (it) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            userViewModel.signInUser(textEmail,textPassword).observeForever {
                                when (it) {
                                    is Resource.Loading -> {
                                    }
                                    is Resource.Success -> {
                                        navController.navigate("mainScreen")

                                        Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT)
                                            .show()
                                    }
                                    is Resource.Error -> {
                                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        is Resource.Error -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }


                navController.navigate("mainScreen")/* Do something! */

                      },
            modifier = Modifier.constrainAs(buttonConfirmRegister) {
                bottom.linkTo(parent.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            },
        ) {
            Text("Confirm Register")
        }

    }
}

@Composable
private fun SignInScreen(
    navController: NavController,
    userViewModel: UserViewModel

){
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (image,textFieldEmail,textFieldPassword,buttonConfirmSignIn,buttonRegister) = createRefs()

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "",
            modifier = Modifier.constrainAs(image){
                top.linkTo(parent.top, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }
        )

        var textEmail by rememberSaveable { mutableStateOf("") }
        var textPassword by rememberSaveable { mutableStateOf("") }

        TextField(
            value = textEmail,
            onValueChange = { textEmail = it },
            label = { Text("Email") },
            singleLine = true,
            modifier = Modifier.constrainAs(textFieldEmail) {
                top.linkTo(image.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }

        )

        TextField(
            value = textPassword,
            onValueChange = { textPassword = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.constrainAs(textFieldPassword) {
                top.linkTo(textFieldEmail.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            }

        )

        Button(
            onClick = { navController.navigate("registerScreen")/* Do something! */ },
            modifier = Modifier.constrainAs(buttonRegister) {
                top.linkTo(textFieldPassword.bottom, margin = 50.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            },
        ) {
            Text("Register")
        }


        val context = LocalContext.current

        Button(
            onClick = {

                userViewModel.signInUser(textEmail,textPassword).observeForever {
                    when (it) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            navController.navigate("mainScreen")

                            Toast.makeText(context, "Registered Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Error -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },




            modifier = Modifier.constrainAs(buttonConfirmSignIn) {
                bottom.linkTo(parent.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            },
        ) {
            Text("Confirm SignIn")
        }

    }
}














