package com.openclassrooms.realestatemanager.data.repository

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.repository.UserRepository
import com.openclassrooms.realestatemanager.domain.models.Response
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
@InternalCoroutinesApi
class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val authUI: AuthUI,
    private val usersRef : CollectionReference,
    private val context: Context?
): UserRepository{

    override suspend fun logout() = flow {
        try {
            emit(Response.Loading)

            authUI.signOut(context!!)

            emit(Response.Success(null))
        }catch (e: Exception){
            emit(Response.Failure(e))
        }
    }

    override suspend fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String) = flow {

            try{
                emit(Response.Loading)

                val registrationResult = firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword).await()

                val user = firebaseAuth.currentUser
                if (user != null) {
                    val urlPicture = if (user.photoUrl != null) user.photoUrl.toString() else null
                    val uid = user.uid
                    val userToCreate = User(uid, userName, urlPicture, userEmailAddress)
                    usersRef.document(uid).set(userToCreate)
                }

                emit(Response.Success(registrationResult))
            }catch (e: Exception){
                emit(Response.Failure(e))
            }

    }


    override suspend fun loginUser(email: String, password: String) = flow {

        try {
            emit(Response.Loading)

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()

            emit(Response.Success(result))
        }catch (e: Exception){
            emit(Response.Failure(e))
        }

    }

    override suspend fun sendPasswordResetEmail(email: String) = flow {

        try {
            emit(Response.Loading)

            firebaseAuth.sendPasswordResetEmail(email).await()

            emit(Response.Success(null))
        }catch (e: Exception){
            emit(Response.Failure(e))
        }

    }

    override suspend fun deleteUser() = flow {
        try {
            emit(Response.Loading)

            val uid = firebaseAuth.uid
            if (uid != null) {
                usersRef.document(uid).delete()
            }
            authUI.delete(context!!)

            emit(Response.Success(null))
        }catch (e: Exception){
            emit(Response.Failure(e))
        }
    }

    override fun userData() = flow {
        try {
            emit(Response.Loading)

            val user = firebaseAuth.currentUser
            val uid = user?.uid

            val userData = usersRef.document(uid!!).get().await().toObject(User::class.java)

            emit(Response.Success(userData))
        }catch (e: Exception){
            emit(Response.Failure(e))
        }
    }

    // Remove the current element from the iterator and the list.
    override fun getUsers() = flow {

        try {
            emit(Response.Loading)

            val list = usersRef.get().await().map {
                it.toObject(User::class.java)
            }.toMutableList()

            val iterator = list.iterator()
            while (iterator.hasNext()) {
                if (iterator.next().uid == firebaseAuth.currentUser?.uid
                ) {
                    // Remove the current element from the iterator and the list.
                    iterator.remove()
                }
            }

            emit(Response.Success(list))
        }catch (e: Exception){
            emit(Response.Failure(e))
        }

    }

    override suspend fun setUserEmail(email: String?) = flow {
        try {
            emit(Response.Loading)

            firebaseAuth.currentUser!!.updateEmail(email!!)
            usersRef.document(firebaseAuth.uid!!).update("email", email)

            emit(Response.Success(null))
        }catch (e : Exception){
            emit(Response.Failure(e))
        }

    }

    override suspend fun setUsername(username: String?) = flow {
        try {
            emit(Response.Loading)

            usersRef.document(firebaseAuth.uid!!).update("username", username)

            emit(Response.Success(null))
        }catch (e : Exception){
            emit(Response.Failure(e))
        }
    }

    override suspend fun setPhotoUrl(photoUrl: String?) = flow {
        try {
            emit(Response.Loading)

            usersRef.document(firebaseAuth.uid!!).update("urlPicture", photoUrl)

            emit(Response.Success(null))
        }catch (e : Exception){
            emit(Response.Failure(e))
        }
    }

}