package com.openclassrooms.realestatemanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(navController: NavController, userViewModel: UserViewModel){
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    ConstraintLayout(modifier = Modifier.fillMaxSize().also {
        it.padding(250.dp,250.dp,250.dp,250.dp)
    }) {
        val (entryEmail,entryPassword,buttonLogin,buttonRegister,textButtonRegister,buttonResetPassword) = createRefs()
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
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            singleLine = true,
            modifier = Modifier.constrainAs(entryPassword) {
                top.linkTo(entryEmail.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)

        )

        Text(text = "Don't have an account ?", modifier = Modifier.constrainAs(textButtonRegister) {
            top.linkTo(buttonLogin.bottom, margin = 15.dp)
            start.linkTo(parent.start, margin = 0.dp)
            end.linkTo(parent.end, margin = 0.dp)
        })

        TextButton(
            onClick = { navController.navigate("registerScreen")/* Do something! */ },
            modifier = Modifier.constrainAs(buttonRegister) {
                top.linkTo(textButtonRegister.bottom, margin = 0.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
            },
        ) {
            Text("REGISTER")
        }

        TextButton(
            onClick = {
                userViewModel.sendPasswordResetEmail(email).observeForever{
                    when (it) {
                        is Resource.Loading -> {
                        }
                        is Resource.Error -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            if(it.data == true){
                                Toast.makeText(context, "Email sent", Toast.LENGTH_SHORT).show()
                            }else{
                                Toast.makeText(context, "Error email not sent", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            },
            modifier = Modifier.constrainAs(buttonResetPassword) {
                top.linkTo(entryPassword.bottom, margin = 0.dp)
                end.linkTo(entryPassword.end, margin = 0.dp)
            },
        ) {
            Text("Forgot password ?")
        }




        Button(
            onClick = {

                userViewModel.loginUser(email,password).observeForever {
                    when (it) {
                        is Resource.Loading -> {
                        }
                        is Resource.Success -> {
                            navController.navigate("mainScreen")

                            Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT)
                                .show()
                        }
                        is Resource.Error -> {
                            Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            },




            modifier = Modifier
                .size(width = 275.dp, height = 50.dp)
                .constrainAs(buttonLogin) {
                    top.linkTo(entryPassword.bottom, margin = 50.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                },
        ) {
            Text("Login")
        }

    }
}