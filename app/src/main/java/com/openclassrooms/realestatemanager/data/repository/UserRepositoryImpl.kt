package com.openclassrooms.realestatemanager.data.repository

import android.content.Context
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.repository.UserRepository
import com.openclassrooms.realestatemanager.domain.models.Response
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val firebaseAuth : FirebaseAuth,
    private val authUI: AuthUI,
    private val firestore: FirebaseFirestore,
    private val context: Context?
): UserRepository{

    override suspend fun logout() : Response<Boolean> {
        return try {
            authUI.signOut(context!!)
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
            authUI.delete(context!!)

            Response.Success(true)
        }catch (e: Exception){
            Response.Failure(e)
        }
    }

    override suspend fun userData() : Response<User?> {
        return try {
            val user = firebaseAuth.currentUser
            val uid = user?.uid
            val userData = firestore.collection("users").document(uid!!).get().await().toObject(User::class.java)
            Response.Success(userData)
        }catch (e: Exception){
            Response.Failure(e)
        }
    }

    // Remove the current element from the iterator and the list.
    override suspend fun getUsers()  : Response<List<User>> {
        return try {
            Response.Loading

            val list = firestore.collection("users").get().await().map {
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

            Response.Success(list)
        }catch (e: Exception){
            Response.Failure(e)
        }
    }

    override suspend fun setUserEmail(email: String?) : Response<Boolean> {
        return try {
            firebaseAuth.currentUser!!.updateEmail(email!!)
            firestore.collection("users").document(firebaseAuth.uid!!).update("email", email)

            Response.Success(true)
        }catch (e : Exception){
            Response.Failure(e)
        }

    }

    override suspend fun setUsername(username: String?) : Response<Boolean> {
        return try {

            firestore.collection("users").document(firebaseAuth.uid!!).update("username", username)

            Response.Success(true)
        }catch (e : Exception){
            Response.Failure(e)
        }
    }

    override suspend fun setPhotoUrl(photoUrl: String?) : Response<Boolean> {
        return try {

            firestore.collection("users").document(firebaseAuth.uid!!).update("urlPicture", photoUrl)

            Response.Success(true)
        }catch (e : Exception){
            Response.Failure(e)
        }
    }

}