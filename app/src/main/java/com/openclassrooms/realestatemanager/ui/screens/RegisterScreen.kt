package com.openclassrooms.realestatemanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.utils.Resource
import com.openclassrooms.realestatemanager.viewmodels.UserViewModel

@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel){
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