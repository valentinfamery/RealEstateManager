package com.openclassrooms.realestatemanager.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.domain.models.Response
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Composable
fun LoginUserCompose(
    navController: NavController,
    responseLogin: Response<AuthResult>
) {

    val context = LocalContext.current

    when(responseLogin){
        is Response.Failure -> {
            Toast.makeText(context,responseLogin.e.toString(), Toast.LENGTH_SHORT).show()
        }
        is Response.Loading ->{}
        is Response.Success -> {
            navController.navigate("mainScreen")
        }
    }
}