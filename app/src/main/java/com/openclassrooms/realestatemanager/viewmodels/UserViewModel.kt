package com.openclassrooms.realestatemanager.viewmodels

import android.content.Context
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repository.UserRepository
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.models.User
import com.openclassrooms.realestatemanager.utils.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel : ViewModel {
    var userRepository: UserRepository

    constructor() {
        userRepository = UserRepository()
    }

    constructor(userRepository: UserRepository) {
        this.userRepository = userRepository
    }

    fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String) : LiveData<Resource<AuthResult>> {

        val userRegistrationStatus = MutableLiveData<Resource<AuthResult>>()

        val error =
            if (userEmailAddress.isEmpty() || userName.isEmpty() || userLoginPassword.isEmpty() ) {
                "Please fill all the fields"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmailAddress).matches()) {
                "Not a valid Email"
            } else null

        error?.let {
            userRegistrationStatus.postValue(Resource.Error(it))
            return userRegistrationStatus
        }
        userRegistrationStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val registerResult = userRepository.registerUser(userName = userName, userEmailAddress = userEmailAddress, userLoginPassword = userLoginPassword)
            userRegistrationStatus.postValue(registerResult)
        }
        return userRegistrationStatus
    }

    fun loginUser(userEmailAddress: String, userLoginPassword: String) : MutableLiveData<Resource<AuthResult>>{
        val userSignUpStatus = MutableLiveData<Resource<AuthResult>>()
        if (userEmailAddress.isEmpty() || userLoginPassword.isEmpty()) {
            userSignUpStatus.postValue(Resource.Error("Please fill all the fields"))
        } else {
            userSignUpStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val loginResult = userRepository.loginUser(userEmailAddress, userLoginPassword)
                userSignUpStatus.postValue(loginResult)
            }
        }
        return userSignUpStatus
    }

    fun sendPasswordResetEmail(userEmailAddress: String) : MutableLiveData<Resource<Void>>{
        val userPasswordResetStatus = MutableLiveData<Resource<Void>>()
        if (userEmailAddress.isEmpty()) {
            userPasswordResetStatus.postValue(Resource.Error("Please fill the email field"))
        } else {
            userPasswordResetStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val sendPasswordResetEmailResult = userRepository.sendPasswordResetEmail(userEmailAddress)
                userPasswordResetStatus.postValue(sendPasswordResetEmailResult)
            }
        }
        return userPasswordResetStatus
    }


    fun signOut(context: Context?) {
        userRepository.logout(context)
    }

    fun deleteUser(context: Context?) {
        userRepository.deleteUser(context)
    }

    val userData: MutableLiveData<User?>
        get() = userRepository.userData
    val getUsers: MutableLiveData<List<User>>
        get() = userRepository.getUsers

    fun setUserName(userName: String?) {
        userRepository.setUsername(userName)
    }

    fun setUserEmail(email: String?) {
        userRepository.setUserEmail(email)
    }

    fun setPhotoUrl(photoUrl: String?) {
        userRepository.setPhotoUrl(photoUrl)
    }
}