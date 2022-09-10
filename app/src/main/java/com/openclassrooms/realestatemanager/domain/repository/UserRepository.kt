package com.openclassrooms.realestatemanager.domain.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.models.Response

interface UserRepository {

    fun logout()

    suspend fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String): Response<AuthResult>

    suspend fun loginUser(email: String, password: String): Response<AuthResult>

    suspend fun sendPasswordResetEmail(email: String): Response<Boolean>

    fun deleteUser()

    fun userData(): MutableLiveData<User?>

    fun getUsers(): MutableLiveData<List<User>>

    fun setUserEmail(email: String?)

    fun setUsername(username: String?)

    fun setPhotoUrl(photoUrl: String?)

}