package com.openclassrooms.realestatemanager.data.repository

import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.firebase.ui.auth.AuthUI
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.QuerySnapshot
import com.openclassrooms.realestatemanager.domain.models.User
import com.openclassrooms.realestatemanager.domain.repository.UserRepository
import com.openclassrooms.realestatemanager.domain.models.Response
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
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

    override fun logout() {
        authUI.signOut(context!!)
    }

    override suspend fun registerUser(userName: String, userEmailAddress: String, userLoginPassword: String): Response<AuthResult> {
        return withContext(Dispatchers.IO) {

            val registrationResult = firebaseAuth.createUserWithEmailAndPassword(userEmailAddress, userLoginPassword).await()
            val user = firebaseAuth.currentUser
            if (user != null) {
                val urlPicture = if (user.photoUrl != null) user.photoUrl.toString() else null
                val uid = user.uid
                val userToCreate = User(uid, userName, urlPicture, userEmailAddress)
                usersRef.document(uid).set(userToCreate)
            }
            Response.Success(registrationResult)
        }
    }

    override suspend fun loginUser(email: String, password: String): Response<AuthResult> {
        return withContext(Dispatchers.IO) {
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            Response.Success(result)
        }
    }

    override suspend fun sendPasswordResetEmail(email: String): Response<Boolean> {
        return withContext(Dispatchers.IO) {
            firebaseAuth.sendPasswordResetEmail(email).await()
            Response.Success(true)
        }
    }

    override fun deleteUser() {
        val uid = firebaseAuth.uid
        if (uid != null) {
            usersRef.document(uid).delete()
        }
        authUI.delete(context!!)
    }

    override fun userData(): MutableLiveData<User?> {
        val user = firebaseAuth.currentUser
        val uid = user?.uid
        val result = MutableLiveData<User?>()

        if (uid != null) {
            usersRef.document(uid).get().addOnSuccessListener { documentSnapshot ->
                val user = documentSnapshot.toObject(User::class.java)
                result.postValue(user)
            }
        }

        return result
    }

    // Remove the current element from the iterator and the list.
    override fun getUsers(): MutableLiveData<List<User>> {
            val result = MutableLiveData<List<User>>()
            usersRef.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
                val userList = queryDocumentSnapshots.toObjects(
                    User::class.java
                )
                val iterator = userList.iterator()
                while (iterator.hasNext()) {
                    if (iterator.next().uid == Objects.requireNonNull(firebaseAuth.currentUser)
                            ?.uid
                    ) {
                        // Remove the current element from the iterator and the list.
                        iterator.remove()
                    }
                }
                result.postValue(userList)
            }
            return result
        }

    override fun setUserEmail(email: String?) {
        Objects.requireNonNull(firebaseAuth.currentUser)!!.updateEmail(email!!)
        usersRef.document(Objects.requireNonNull(firebaseAuth.uid)!!).update("email", email)
    }

    override fun setUsername(username: String?) {
        usersRef.document(Objects.requireNonNull(firebaseAuth.uid)!!)
            .update("username", username)
    }

    override fun setPhotoUrl(photoUrl: String?) {
        usersRef.document(Objects.requireNonNull(firebaseAuth.uid)!!)
            .update("urlPicture", photoUrl)
    }

}