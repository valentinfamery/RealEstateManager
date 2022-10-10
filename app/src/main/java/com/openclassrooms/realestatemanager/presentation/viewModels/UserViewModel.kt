package com.openclassrooms.realestatemanager.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {

    var logoutResponse by mutableStateOf<Response<Boolean>>(Response.Loading)

    var registerUserResponse by mutableStateOf<Response<AuthResult>>(Response.Loading)

    var loginUserResponse by mutableStateOf<Response<AuthResult>>(Response.Loading)

    var sendPasswordResetEmailResponse by mutableStateOf<Response<Boolean>>(Response.Success(true))

    var deleteUserResponse by mutableStateOf<Response<Boolean>>(Response.Success(true))

    var userDataResponse by mutableStateOf<Response<User?>>(Response.Loading)

    var usersResponse by mutableStateOf<Response<List<User>>>(Response.Loading)

    var setUsernameResponse by mutableStateOf<Response<Boolean>>(Response.Success(true))

    var setUserEmailResponse by mutableStateOf<Response<Boolean>>(Response.Success(true))

    var setPhotoUrlResponse by mutableStateOf<Response<Boolean>>(Response.Success(true))

    fun logout() = viewModelScope.launch {
        logoutResponse = useCases.logout()
    }

    fun registerUser(
        userName: String,
        userEmailAddress: String,
        userLoginPassword: String
    ) = viewModelScope.launch {
        registerUserResponse = useCases.registerUser(userName = userName, userEmailAddress = userEmailAddress, userLoginPassword = userLoginPassword)
    }

    fun loginUser(userEmailAddress: String, userLoginPassword: String) = viewModelScope.launch {
        loginUserResponse = Response.Loading
        loginUserResponse = useCases.loginUser(userEmailAddress, userLoginPassword)
    }

    fun sendPasswordResetEmail(userEmailAddress: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = useCases.sendPasswordResetEmail(userEmailAddress)
    }

    fun deleteUser() = viewModelScope.launch{
        deleteUserResponse = useCases.deleteUser()
    }

    fun userData() = viewModelScope.launch {
        useCases.userData().collect{ response ->
            userDataResponse = response
        }
    }

    fun getUsers() = viewModelScope.launch {
        useCases.getUsers().collect{ response ->
            usersResponse = response
        }
    }

    fun setUserName(userName: String?) = viewModelScope.launch {
        setUsernameResponse = useCases.setUsername(userName)
    }

    fun setUserEmail(email: String?) = viewModelScope.launch {
        setUserEmailResponse = useCases.setUserEmail(email)
    }

    fun setPhotoUrl(photoUrl: String?) = viewModelScope.launch {
        setPhotoUrlResponse = useCases.setPhotoUrl(photoUrl)
    }
}