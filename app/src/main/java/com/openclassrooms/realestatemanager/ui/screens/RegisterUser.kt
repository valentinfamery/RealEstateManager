package com.openclassrooms.realestatemanager.ui.screens

import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@OptIn(InternalCoroutinesApi::class)
@Composable
fun RegisterUser(responseRegisterUser: Response<AuthResult>, navController: NavController,email : String,password : String,userViewModel: UserViewModel = hiltViewModel()) {

    val context = LocalContext.current

    val responseLoginUser = userViewModel.loginUserResponse

    when(responseRegisterUser){
        is Response.Failure -> {
            Toast.makeText(context,responseRegisterUser.e.toString(), Toast.LENGTH_SHORT).show()
        }
        is Response.Loading ->{

        }
        is Response.Success -> {
            userViewModel.loginUser(email,password)


        }
    }

    LoginUser(navController,responseLoginUser)

}