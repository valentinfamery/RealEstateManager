package com.openclassrooms.realestatemanager.ui.screens

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterEmailUsernamePassword(
    isExpanded: Boolean,
    userViewModel: UserViewModel,
    navController: NavController
) {
    var email by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    val context = LocalContext.current

    var password by rememberSaveable { mutableStateOf("") }
    var password2 by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var passwordVisible2 by rememberSaveable { mutableStateOf(false) }

    val icon = if(passwordVisible)
        painterResource(id = R.drawable.ic_baseline_visibility_24 )
    else
        painterResource(id = R.drawable.ic_baseline_visibility_off_24)

    val icon2 = if(passwordVisible2)
        painterResource(id = R.drawable.ic_baseline_visibility_24 )
    else
        painterResource(id = R.drawable.ic_baseline_visibility_off_24)

    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (centerAlignedTopAppBar, entryEmail, entryUsername, entryPassword,entryPassword2, buttonRegister) = createRefs()


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
                width = if(!isExpanded) Dimension.percent(0.8f) else Dimension.percent(0.3f)
                height = Dimension.wrapContent
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
                width = if(!isExpanded) Dimension.percent(0.8f) else Dimension.percent(0.3f)
                height = Dimension.wrapContent
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
                width = if(!isExpanded) Dimension.percent(0.8f) else Dimension.percent(0.3f)
                height = Dimension.wrapContent
            },
            visualTransformation = if(passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { IconButton(onClick = {
                passwordVisible = !passwordVisible
            }) {
                Icon(painter = icon, contentDescription ="" )
            }
            }

        )

        TextField(
            value = password2,
            onValueChange = { password2 = it },
            label = { Text("Confirm Password") },
            singleLine = true,
            modifier = Modifier.constrainAs(entryPassword2) {
                top.linkTo(entryPassword.bottom, margin = 25.dp)
                start.linkTo(parent.start, margin = 0.dp)
                end.linkTo(parent.end, margin = 0.dp)
                width = if(!isExpanded) Dimension.percent(0.8f) else Dimension.percent(0.3f)
                height = Dimension.wrapContent
            },
            visualTransformation = if(passwordVisible2) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = { IconButton(onClick = {
                passwordVisible2 = !passwordVisible2
            }) {
                Icon(painter = icon2, contentDescription ="" )
            }
            }

        )

        Button(
            onClick = {
                if(password == password2)
                    userViewModel.registerUser(username, email, password)
                else
                    Toast.makeText(context, "Passwords Not matched", Toast.LENGTH_SHORT).show()
            },
            modifier = Modifier
                .size(width = 275.dp, height = 50.dp)
                .constrainAs(buttonRegister) {
                    top.linkTo(entryPassword2.bottom, margin = 25.dp)
                    start.linkTo(parent.start, margin = 0.dp)
                    end.linkTo(parent.end, margin = 0.dp)
                },
        ) {
            Text("Register")
        }
    }
}