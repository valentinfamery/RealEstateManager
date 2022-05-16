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
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class UserViewModel : ViewModel {
    var userRepository: UserRepository

    constructor() {
        userRepository = UserRepository()
    }

    constructor(userRepository: UserRepository) {
        this.userRepository = userRepository
    }


    fun createUser(userName: String, userEmailAddress: String, userLoginPassword: String) : MutableLiveData<Resource<AuthResult>> {

        val _userRegistrationStatus = MutableLiveData<Resource<AuthResult>>()

        var error =
            if (userEmailAddress.isEmpty() || userName.isEmpty() || userLoginPassword.isEmpty() ) {
                "Empty Strings"
            } else if (!Patterns.EMAIL_ADDRESS.matcher(userEmailAddress).matches()) {
                "Not a valid Email"
            } else null

        error?.let {
            _userRegistrationStatus.postValue(Resource.Error(it))
            return _userRegistrationStatus
        }
        _userRegistrationStatus.postValue(Resource.Loading())

        viewModelScope.launch(Dispatchers.Main) {
            val registerResult = userRepository.createUser(userName = userName, userEmailAddress = userEmailAddress, userLoginPassword = userLoginPassword)
            _userRegistrationStatus.postValue(registerResult)


        }
        return _userRegistrationStatus
    }

    fun signInUser(userEmailAddress: String, userLoginPassword: String) : MutableLiveData<Resource<AuthResult>>{

        val _userSignUpStatus = MutableLiveData<Resource<AuthResult>>()

        if (userEmailAddress.isEmpty() || userLoginPassword.isEmpty()) {
            _userSignUpStatus.postValue(Resource.Error("Empty Strings"))
        } else {

            _userSignUpStatus.postValue(Resource.Loading())
            viewModelScope.launch(Dispatchers.Main) {
                val loginResult = userRepository.login(userEmailAddress, userLoginPassword)
                _userSignUpStatus.postValue(loginResult)
            }
        }
        return _userSignUpStatus
    }



    val isCurrentUserLoggedIn: MutableLiveData<Boolean>
        get() = userRepository.isCurrentUserLoggedIn

    fun signOut(context: Context?) {
        userRepository.signOut(context)
    }

    fun deleteUser(context: Context?) {
        userRepository.deleteUser(context)
    }






    val userData: MutableLiveData<User?>
        get() = userRepository.userData
    val allUsers: MutableLiveData<List<User>>
        get() = userRepository.allUsers

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