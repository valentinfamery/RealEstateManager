package com.openclassrooms.realestatemanager.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(navController: NavController, userViewModel: UserViewModel){
    var email by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    Log.e("recompose","registerScreen")

    when(val responseRegisterUser = userViewModel.registerUserResponse) {
        is Response.Empty -> {
            ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                val (centerAlignedTopAppBar, entryEmail, entryUsername, entryPassword, buttonRegister) = createRefs()


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

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Email") },
                    singleLine = true,
                    modifier = Modifier.constrainAs(entryEmail) {
                        top.linkTo(parent.top, margin = 250.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }

                )

                TextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Username") },
                    singleLine = true,
                    modifier = Modifier.constrainAs(entryUsername) {
                        top.linkTo(entryEmail.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }

                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    modifier = Modifier.constrainAs(entryPassword) {
                        top.linkTo(entryUsername.bottom, margin = 25.dp)
                        start.linkTo(parent.start, margin = 0.dp)
                        end.linkTo(parent.end, margin = 0.dp)
                    }

                )

                Button(
                    onClick = {
                        userViewModel.registerUser(username, email, password)
                    },
                    modifier = Modifier
                        .size(width = 275.dp, height = 50.dp)
                        .constrainAs(buttonRegister) {
                            top.linkTo(entryPassword.bottom, margin = 25.dp)
                            start.linkTo(parent.start, margin = 0.dp)
                            end.linkTo(parent.end, margin = 0.dp)
                        },
                ) {
                    Text("Register")
                }
            }
        }
        is Response.Failure -> {
            Log.i("responseRegisterUser", "Failure")
            Toast.makeText(context, responseRegisterUser.e.toString(), Toast.LENGTH_SHORT).show()
            Log.i("responseRegisterUser", responseRegisterUser.e.toString())
        }
        is Response.Loading -> {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
        is Response.Success -> {
            Log.i("responseRegisterUser", "Success")
            LaunchedEffect(responseRegisterUser){
                navController.navigate("mainScreen")
            }
        }
    }

}