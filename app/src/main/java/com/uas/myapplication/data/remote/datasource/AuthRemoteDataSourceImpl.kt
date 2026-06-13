@file:Suppress("DEPRECATION")
package com.uas.myapplication.data.remote.datasource

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

/**
 * Implementasi AuthRemoteDataSource yang berinteraksi langsung
 * dengan Firebase Auth SDK dan Google Sign-In.
 */
class AuthRemoteDataSourceImpl(
    private val auth: FirebaseAuth,
    private val context: Context
) : AuthRemoteDataSource {

    override suspend fun loginWithEmail(email: String, password: String): String {
        val authResult = auth.signInWithEmailAndPassword(email, password).await()
        return authResult.user?.uid ?: throw Exception("UID tidak ditemukan")
    }

    @Suppress("DEPRECATION")
    override suspend fun loginWithGoogle(idToken: String): Pair<String, Boolean> {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        val authResult = auth.signInWithCredential(credential).await()
        val firebaseUser = authResult.user ?: throw Exception("Login Google gagal")
        val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
        return Pair(firebaseUser.uid, isNewUser)
    }

    override suspend fun registerWithEmail(email: String, password: String): String {
        val authResult = auth.createUserWithEmailAndPassword(email, password).await()
        return authResult.user?.uid ?: throw Exception("UID tidak ditemukan")
    }

    override suspend fun logout() {
        auth.signOut()

        val gso = GoogleSignInOptions.Builder(
            GoogleSignInOptions.DEFAULT_SIGN_IN
        )
            .requestEmail()
            .build()

        GoogleSignIn.getClient(context, gso).signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }

    override fun getCurrentUserEmail(): String? {
        return auth.currentUser?.email
    }

    override fun getCurrentUserDisplayName(): String? {
        return auth.currentUser?.displayName
    }
}
