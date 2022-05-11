package com.openclassrooms.realestatemanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
fun SignInScreen(
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

                            Toast.makeText(context, "SignIn Successfully", Toast.LENGTH_SHORT)
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