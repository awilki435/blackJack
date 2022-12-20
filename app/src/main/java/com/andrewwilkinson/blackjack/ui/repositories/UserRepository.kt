package com.andrewwilkinson.blackjack.ui.repositories

import com.andrewwilkinson.blackjack.ui.models.User
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SignInException(message: String?): RuntimeException(message)
class SignUpException(message: String?): RuntimeException(message)

object UserRepository {

    var userCache = User()
    private val allUsersCache = mutableListOf<User>()

    suspend fun createUser(email: String, password: String, name: String, money: Int) {
        try {
            Firebase.auth.createUserWithEmailAndPassword(
                email,
                password
            ).await()
            val testDoc = Firebase.firestore.collection("users").whereEqualTo("email", email)
            var doc = Firebase.firestore.collection("users").document()
            if (testDoc.get().await().isEmpty) {
                doc.set(
                    User(
                        name = name,
                        email = email,
                        userId = getCurrentUserId(),
                        id = doc.id,
                        money = money
                    )
                )
            } else {
                doc = testDoc.get().await().documents[0].reference
                doc.set(
                    User(
                        name = name,
                        email = email,
                        userId = getCurrentUserId(),
                        id = doc.id,
                        money = 50000
                    )
                )
            }
            userCache = doc.get().await().toObject()!!
        } catch (e: FirebaseAuthException) {
            throw SignUpException(e.message)
        }
    }

    suspend fun updateUser(user: User) {
        try {
            Firebase.firestore.collection("users").document(user.id!!).set(user).await()
            val oldUserIndex = allUsersCache.indexOfFirst { it.id == user.id }
            allUsersCache[oldUserIndex] = user
            if (userCache.id == user.id) {
                userCache = user
            }
        } catch (_: Exception) {
        }
    }

    suspend fun loginUser(email: String, password: String) {
        try {
            var user: User
            Firebase.auth.signInWithEmailAndPassword(
                email,
                password
            ).await()
            Firebase.firestore
                .collection("users")
                .whereEqualTo("userId", getCurrentUserId())
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    for (document in documentSnapshot) {
                        user = document.toObject()
                        userCache = user
                    }
                }
                .await()
        } catch (e: FirebaseAuthException) {
            throw SignInException(e.message)
        }
    }

    suspend fun setUserCache() {
        try {
            var user: User
            Firebase.firestore
                .collection("users")
                .whereEqualTo("userId", getCurrentUserId())
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    for (document in documentSnapshot) {
                        user = document.toObject()
                        userCache = user
                    }
                }
                .await()
        } catch (e: FirebaseAuthException) {
            throw SignInException(e.message)
        }
    }

    suspend fun getAllUsers(): List<User> {
        try {
            if (allUsersCache.isEmpty()) {
                val snapshot = Firebase.firestore
                    .collection("users")
                    .get()
                    .await()
                allUsersCache.addAll(snapshot.toObjects())
            }

            return allUsersCache
        } catch (e: FirebaseAuthException) {
            throw SignInException(e.message)
        }
    }

    suspend fun deleteUser(user: User) {
        try {
            // Only delete the user from Firestore, not from authentication
            // That way they can still sign in and access their account, but it is wiped
            Firebase.firestore
                .collection("users")
                .document(user.id!!)
                .delete()
                .await()
        } catch (e: FirebaseAuthException) {
            throw SignInException(e.message)
        }
    }
    fun getCurrentUser(): User {
        return userCache
    }

    fun getCurrentUserId(): String? {
        return Firebase.auth.currentUser?.uid
    }
    fun signOutUser() {
        try {
            userCache = User()
            Firebase.auth.signOut()
        } catch (_: Exception){}
    }
    fun userCoins(): Int{
        return userCache.money!!
    }
    fun isUserLoggedIn(): Boolean {
        return getCurrentUserId() != null
    }
}