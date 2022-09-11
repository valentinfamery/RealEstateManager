package com.openclassrooms.realestatemanager.domain.repository

import com.google.firebase.auth.AuthResult
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.models.Response
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun logout() : Flow<Response<Void?>>

    suspend fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String): Flow<Response<AuthResult>>

    suspend fun loginUser(email: String, password: String): Flow<Response<AuthResult>>

    suspend fun sendPasswordResetEmail(email: String): Flow<Response<Void?>>

    suspend fun deleteUser() : Flow<Response<Void?>>

    fun userData(): Flow<Response<User?>>

    fun getUsers(): Flow<Response<List<User>>>

    suspend fun setUserEmail(email: String?) : Flow<Response<Void?>>

    suspend fun setUsername(username: String?) : Flow<Response<Void?>>

    suspend fun setPhotoUrl(photoUrl: String?) : Flow<Response<Void?>>

}