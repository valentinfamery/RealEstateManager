package com.openclassrooms.realestatemanager.data.repository

import android.content.Context
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.repository.UserRepository
import com.openclassrooms.realestatemanager.domain.models.Response
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val authUI: AuthUI,
    private val firestore: FirebaseFirestore,
    private val context: Context
): UserRepository{

    override suspend fun logout() : Response<Boolean> {
        return try {
            authUI.signOut(context)
            Response.Success(true)
        }catch (e: Exception){
            Response.Failure(e)
        }
    }

    override suspend fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String) : Response<AuthResult> {

            return try{
                val registrationResult = firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword).await()

                val user = firebaseAuth.currentUser
                if (user != null) {
                    val urlPicture = if (user.photoUrl != null) user.photoUrl.toString() else null
                    val uid = user.uid
                    val userToCreate = User(uid, userName, urlPicture, userEmailAddress)
                    firestore.collection("users").document(uid).set(userToCreate)
                }

                Response.Success(registrationResult)
            }catch (e: Exception){
                Response.Failure(e)
            }

    }


    override suspend fun loginUser(email: String, password: String) : Response<AuthResult> {

        return try {

            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Response.Success(result)

        }catch (e: Exception){
            Response.Failure(e)
        }

    }

    override suspend fun sendPasswordResetEmail(email: String) : Response<Boolean> {

        return try {

            firebaseAuth.sendPasswordResetEmail(email).await()

            Response.Success(true)
        }catch (e: Exception){
            Response.Failure(e)
        }

    }

    override suspend fun deleteUser() : Response<Boolean> {
        return try {

            val uid = firebaseAuth.uid
            if (uid != null) {
                firestore.collection("users").document(uid).delete()
            }
            authUI.delete(context)

            Response.Success(true)
        }catch (e: Exception){
            Response.Failure(e)
        }
    }

    override suspend fun userData() : User? {
        if(firebaseAuth.currentUser != null) {
            val user = firebaseAuth.currentUser
            val uid = user?.uid
            Log.e("userData()", "uid" + uid.toString())
            val userData =
                firestore.collection("users").document(uid!!).get().await()
                    .toObject(User::class.java)
            Log.e("userData()", "userData" + userData?.username.toString())
            return userData
        }else {
            return null
        }

    }
}