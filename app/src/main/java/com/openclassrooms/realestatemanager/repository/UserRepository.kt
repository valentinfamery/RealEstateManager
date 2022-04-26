package com.openclassrooms.realestatemanager.repository

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.firebase.ui.auth.AuthUI
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.openclassrooms.realestatemanager.models.User
import java.util.*

class UserRepository {
    var firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    var authUI: AuthUI = AuthUI.getInstance()
    val isCurrentUserLoggedIn: MutableLiveData<Boolean>
        get() {
            val result = MutableLiveData<Boolean>()
            result.postValue(firebaseAuth.currentUser != null)
            return result
        }

    fun signOut(context: Context?) {
        authUI.signOut(context!!)
    }

    fun deleteUser(context: Context?) {
        val uid = firebaseAuth.uid
        if (uid != null) {
            usersCollection.document(uid).delete()
        }
        authUI.delete(context!!)
    }

    // Get the Collection Reference
    private val usersCollection: CollectionReference
        private get() = FirebaseFirestore.getInstance().collection(COLLECTION_USERS)

    // Create User in Firestore
    fun createUser() {
        val user = firebaseAuth.currentUser
        if (user != null) {
            val urlPicture = if (user.photoUrl != null) user.photoUrl.toString() else null
            val username = user.displayName
            val uid = user.uid
            val email = user.email
            val userToCreate = User(uid, username, urlPicture, email)
            usersCollection.document(uid).set(userToCreate)
        }
    }

    val userData: MutableLiveData<User?>
        get() {
            val user = firebaseAuth.currentUser
            val uid = Objects.requireNonNull(user)!!.uid
            val result = MutableLiveData<User?>()
            val docRef = usersCollection.document(uid)
            docRef.get().addOnSuccessListener { documentSnapshot: DocumentSnapshot ->
                val user1 = documentSnapshot.toObject(
                    User::class.java
                )
                result.postValue(user1)
            }
            return result
        }

    // Remove the current element from the iterator and the list.
    val allUsers: MutableLiveData<List<User>>
        get() {
            val result = MutableLiveData<List<User>>()
            usersCollection.get().addOnSuccessListener { queryDocumentSnapshots: QuerySnapshot ->
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

    fun setUserEmail(email: String?) {
        Objects.requireNonNull(firebaseAuth.currentUser)!!.updateEmail(email!!)
        usersCollection.document(Objects.requireNonNull(firebaseAuth.uid)!!).update("email", email)
    }

    fun setUsername(username: String?) {
        usersCollection.document(Objects.requireNonNull(firebaseAuth.uid)!!)
            .update("username", username)
    }

    fun setPhotoUrl(photoUrl: String?) {
        usersCollection.document(Objects.requireNonNull(firebaseAuth.uid)!!)
            .update("urlPicture", photoUrl)
    }

    companion object {
        private const val COLLECTION_USERS = "users"
    }

}