package com.openclassrooms.realestatemanager.presentation.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.*
import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.domain.models.Response
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(private val userRepository: UserRepository) : ViewModel() {

    var registerUserResponse by mutableStateOf<Response<AuthResult>>(Response.Empty)
    var loginUserResponse by mutableStateOf<Response<AuthResult>>(Response.Empty)
    var sendPasswordResetEmailResponse by mutableStateOf<Response<Boolean>>(Response.Empty)
    var deleteUserResponse by mutableStateOf<Response<Boolean>>(Response.Success(true))

    val userData: StateFlow<User?> = flow {
        emit(userRepository.userData())
    }.stateIn(viewModelScope, WhileSubscribed(5000),null)

    fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String) = viewModelScope.launch {
        registerUserResponse = Response.Loading
        registerUserResponse = userRepository.registerUser(userName = userName, userEmailAddress = userEmailAddress, userLoginPassword = userLoginPassword)

    }

    fun loginUser(userEmailAddress: String, userLoginPassword: String) = viewModelScope.launch {
        loginUserResponse = Response.Loading
        loginUserResponse = userRepository.loginUser(userEmailAddress, userLoginPassword)

    }

    fun sendPasswordResetEmail(userEmailAddress: String) = viewModelScope.launch {
        sendPasswordResetEmailResponse = userRepository.sendPasswordResetEmail(userEmailAddress)
    }

    fun deleteUser() = viewModelScope.launch{
        deleteUserResponse = userRepository.deleteUser()
    }
}