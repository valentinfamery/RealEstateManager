package com.openclassrooms.realestatemanager.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel

@Composable
fun RegisterUser(navController: NavController,email : String,password : String,userViewModel: UserViewModel) {

    val context = LocalContext.current

    when(val responseRegisterUser = userViewModel.registerUserResponse){
        is Response.Failure -> {
            Log.i("responseRegisterUser","Failure")
            Toast.makeText(context,responseRegisterUser.e.toString(), Toast.LENGTH_SHORT).show()
        }
        is Response.Loading ->{
            Log.i("responseRegisterUser","Loading")
            CircularProgressIndicator(progress = 0.75f)
        }
        is Response.Success -> {
            userViewModel.loginUser(email,password)
        }
        else ->{}
    }

    when (val loginResponse = userViewModel.loginUserResponse) {
        is Response.Empty -> {}
        is Response.Failure -> {
            Toast.makeText(context, loginResponse.e.toString(), Toast.LENGTH_SHORT).show()
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
            LaunchedEffect(userViewModel.loginUserResponse) {
                if (userViewModel.loginUserResponse is Response.Success) {
                    Toast.makeText(context, "Connexion Réussi", Toast.LENGTH_SHORT).show()
                    navController.navigate("mainScreen")
                }
            }
        }

    }



}