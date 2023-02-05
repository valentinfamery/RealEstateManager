package com.openclassrooms.realestatemanager.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.presentation.viewModels.EstateViewModel
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignInScreen(
    isExpanded: Boolean,
    navigateToMainScreen: () -> Unit,
    navigateToRegisterScreen: () -> Unit
){

    val userViewModel = hiltViewModel<UserViewModel>()
    val estateViewModel = hiltViewModel<EstateViewModel>()

    Log.e("recompose","LoginScreen")

    val context = LocalContext.current

        when (val loginResponse = userViewModel.loginUserResponse) {
            is Response.Empty -> {
                LoginEmailAndPassword(isExpanded,userViewModel) {
                    navigateToRegisterScreen()
                }
            }
            is Response.Failure -> {
                Toast.makeText(context, loginResponse.e.toString(), Toast.LENGTH_SHORT).show()
                LoginEmailAndPassword(isExpanded, userViewModel) {
                    navigateToRegisterScreen()
                }
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
                val successLoginMessage = stringResource(R.string.successLoginMessage)
                estateViewModel.refreshRealEstates()
                LaunchedEffect(userViewModel.loginUserResponse) {
                    if (userViewModel.loginUserResponse is Response.Success) {
                        Toast.makeText(context,successLoginMessage, Toast.LENGTH_SHORT).show()
                        navigateToMainScreen()
                    }
                }
            }

    }


}



