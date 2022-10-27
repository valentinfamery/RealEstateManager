package com.openclassrooms.realestatemanager.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
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
            Log.i("responseRegisterUser","Success")
            userViewModel.loginUser(email,password)
        }
        else ->{}
    }



}