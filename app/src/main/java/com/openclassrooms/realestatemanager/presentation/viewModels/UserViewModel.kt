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
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
@InternalCoroutinesApi
class UserViewModel @Inject constructor(private val useCases: UseCases) : ViewModel() {

    private var logoutResponse by mutableStateOf<Response<Void?>>(Response.Success(null))

    private var registerUserResponse by mutableStateOf<Response<AuthResult>>(Response.Loading)

    var loginUserResponse by mutableStateOf<Response<AuthResult>>(Response.Loading)

    private var sendPasswordResetEmailResponse by mutableStateOf<Response<Void?>>(Response.Success(null))

    private var deleteUserResponse by mutableStateOf<Response<Void?>>(Response.Success(null))

    var userDataResponse by mutableStateOf<Response<User?>>(Response.Loading)

    var usersResponse by mutableStateOf<Response<List<User>>>(Response.Loading)

    private var setUsernameResponse by mutableStateOf<Response<Void?>>(Response.Success(null))

    private var setUserEmailResponse by mutableStateOf<Response<Void?>>(Response.Success(null))

    private var setPhotoUrlResponse by mutableStateOf<Response<Void?>>(Response.Success(null))

    fun logout() = viewModelScope.launch {
        useCases.logout.invoke().collect{ response ->
            logoutResponse = response
        }
    }

    fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String) = viewModelScope.launch {
        useCases.registerUser(userName = userName, userEmailAddress = userEmailAddress, userLoginPassword = userLoginPassword).collect{ response ->
            registerUserResponse = response
        }
    }

    fun loginUser(userEmailAddress: String, userLoginPassword: String) = viewModelScope.launch {
        useCases.loginUser(userEmailAddress, userLoginPassword).collect{ response ->
            loginUserResponse = response
        }
    }

    fun sendPasswordResetEmail(userEmailAddress: String) = viewModelScope.launch {
        useCases.sendPasswordResetEmail(userEmailAddress).collect{ response ->
            sendPasswordResetEmailResponse = response
        }
    }

    fun deleteUser() = viewModelScope.launch{
        useCases.deleteUser.invoke().collect{ response ->
            deleteUserResponse = response
        }
    }

    fun userData() = viewModelScope.launch {
        useCases.userData.invoke().collect{ response ->
            userDataResponse = response
        }
    }

    fun getUsers() = viewModelScope.launch {
        useCases.getUsers.invoke().collect{ response ->
            usersResponse = response
        }
    }

    fun setUserName(userName: String?) = viewModelScope.launch {
        useCases.setUsername.invoke(userName).collect{ response ->
            setUsernameResponse = response
        }
    }

    fun setUserEmail(email: String?) = viewModelScope.launch {
        useCases.setUserEmail.invoke(email).collect{ response ->
            setUserEmailResponse = response
        }
    }

    fun setPhotoUrl(photoUrl: String?) = viewModelScope.launch {
        useCases.setPhotoUrl.invoke(photoUrl).collect{ response ->
            setPhotoUrlResponse = response
        }
    }
}