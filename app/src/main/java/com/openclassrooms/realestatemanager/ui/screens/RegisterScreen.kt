package com.openclassrooms.realestatemanager.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(isExpanded: Boolean, navController: NavController, userViewModel: UserViewModel){

    val context = LocalContext.current


    when(val responseRegisterUser = userViewModel.registerUserResponse) {
        is Response.Empty -> {
            RegisterEmailUsernamePassword(isExpanded,userViewModel,navController)
        }
        is Response.Failure -> {
            Log.i("responseRegisterUser", "Failure")
            Toast.makeText(context, responseRegisterUser.e.toString(), Toast.LENGTH_SHORT).show()
            Log.i("responseRegisterUser", responseRegisterUser.e.toString())
            RegisterEmailUsernamePassword(isExpanded, userViewModel, navController)
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