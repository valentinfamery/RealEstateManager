package com.openclassrooms.realestatemanager.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.openclassrooms.realestatemanager.repository.UserRepository
import androidx.lifecycle.MutableLiveData
import com.openclassrooms.realestatemanager.models.User

class UserViewModel : ViewModel {
    var userRepository: UserRepository

    constructor() {
        userRepository = UserRepository()
    }

    constructor(userRepository: UserRepository) {
        this.userRepository = userRepository
    }

    val isCurrentUserLoggedIn: MutableLiveData<Boolean>
        get() = userRepository.isCurrentUserLoggedIn

    fun signOut(context: Context?) {
        userRepository.signOut(context)
    }

    fun deleteUser(context: Context?) {
        userRepository.deleteUser(context)
    }

    fun createUser() {
        userRepository.createUser()
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