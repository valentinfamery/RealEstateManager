package com.openclassrooms.realestatemanager.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@Composable
fun LoginUser(
    userViewModel: UserViewModel,
    navController: NavController
) {
    val context = LocalContext.current

        when (val responseLogin = userViewModel.loginUserResponse) {
            is Response.Failure -> {
                Log.e("responseLogin", "Failure")
                Toast.makeText(context, responseLogin.e.toString(), Toast.LENGTH_SHORT).show()
            }
            is Response.Loading -> {
                Log.e("responseLogin", "Loading")
            }
            is Response.Success -> {
                Log.e("responseLogin", "Success")
                Toast.makeText(context, "Connexion Réussi", Toast.LENGTH_SHORT).show()

                LaunchedEffect(Unit){
                    Log.e("navigate", "enable")
                    navController.navigate("mainScreen")
                }
                

            }
        }
}