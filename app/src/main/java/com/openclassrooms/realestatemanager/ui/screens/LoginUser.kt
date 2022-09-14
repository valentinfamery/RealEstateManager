package com.openclassrooms.realestatemanager.ui.screens

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.use_case.LoginUser
import com.openclassrooms.realestatemanager.presentation.viewModels.UserViewModel
import kotlinx.coroutines.InternalCoroutinesApi

@InternalCoroutinesApi
@Composable
fun LoginUserCompose(
    userViewModel: UserViewModel = hiltViewModel(),
    loadingEnabled : () -> Unit,
    successLoginUser: () -> Unit,
    failureLoginUser: (e:String) -> Unit
) {
    when(val loginUserResponse = userViewModel.loginUserResponse){
        is Response.Failure -> failureLoginUser(loginUserResponse.e.toString())
        is Response.Loading -> loadingEnabled
        is Response.Success -> successLoginUser
    }
}