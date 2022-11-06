package com.openclassrooms.realestatemanager.presentation.viewModels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.repository.UserRepository
import com.openclassrooms.realestatemanager.domain.use_case.UseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val useCases: UseCases,private val state: SavedStateHandle,private val userRepository: UserRepository) : ViewModel() {

    var registerUserResponse by mutableStateOf<Response<AuthResult>>(Response.Empty)
    var loginUserResponse by mutableStateOf<Response<AuthResult>>(Response.Empty)
    var sendPasswordResetEmailResponse by mutableStateOf<Response<Boolean>>(Response.Empty)
    var deleteUserResponse by mutableStateOf<Response<Boolean>>(Response.Success(true))
    var userDataResponse by mutableStateOf<Response<User?>>(Response.Empty)


    init{
        userData()
    }

    fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String) = viewModelScope.launch {
        registerUserResponse = Response.Loading
        registerUserResponse = useCases.registerUser(userName = userName, userEmailAddress = userEmailAddress, userLoginPassword = userLoginPassword)
        userData()
    }

    fun loginUser(userEmailAddress: String, userLoginPassword: String) = viewModelScope.launch {
        loginUserResponse = Response.Loading
        loginUserResponse = useCases.loginUser(userEmailAddress, userLoginPassword)
        userData()
    }

    fun sendPasswordResetEmail(userEmailAddress: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = useCases.sendPasswordResetEmail(userEmailAddress)
    }

    fun deleteUser() = viewModelScope.launch{
        deleteUserResponse = useCases.deleteUser()
    }

    fun userData() = viewModelScope.launch {
            userDataResponse = useCases.userData()
    }
}