package com.openclassrooms.realestatemanager.domain.repository

import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun logout() : Response<Boolean>

    suspend fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String): Response<AuthResult>

    suspend fun loginUser(email: String, password: String): Response<AuthResult>

    suspend fun sendPasswordResetEmail(email: String): Response<Boolean>

    suspend fun deleteUser() : Response<Boolean>

    suspend fun userData(): Response<User?>

    suspend fun getUsers(): Response<List<User>>

    suspend fun setUserEmail(email: String?) : Response<Boolean>

    suspend fun setUsername(username: String?) : Response<Boolean>

    suspend fun setPhotoUrl(photoUrl: String?) : Response<Boolean>

}