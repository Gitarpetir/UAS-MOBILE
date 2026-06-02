package com.uas.myapplication.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.uas.myapplication.data.remote.dto.UserDto
import com.uas.myapplication.data.remote.dto.toMap
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.AuthRepository
import kotlinx.coroutines.tasks.await

class AuthRepositoryImpl(
    private val auth     : FirebaseAuth,
    private val firestore: FirebaseFirestore
) : AuthRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun loginWithEmail(email: String, password: String): Result<User> {
        return try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("UID tidak ditemukan")
            val snapshot = usersCollection.document(uid).get().await()
            val userDto = snapshot.toObject(UserDto::class.java)
                ?: throw Exception("Data pengguna tidak ditemukan")
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(idToken: String): Result<User> {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val authResult = auth.signInWithCredential(credential).await()
            val firebaseUser = authResult.user ?: throw Exception("Login Google gagal")
            val snapshot = usersCollection.document(firebaseUser.uid).get().await()

            if (snapshot.exists()) {
                val userDto = snapshot.toObject(UserDto::class.java)
                    ?: throw Exception("Data pengguna tidak ditemukan")
                Result.success(userDto.toDomain())
            } else {
                // Pengguna baru via Google — NIM & WhatsApp diisi di Lengkapi Profil
                val newUser = User(
                    uid         = firebaseUser.uid,
                    namaLengkap = firebaseUser.displayName ?: "",
                    email       = firebaseUser.email ?: "",
                    peran       = "mahasiswa"
                )
                usersCollection.document(newUser.uid).set(newUser.toMap()).await()
                Result.success(newUser)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(
        namaLengkap  : String,
        nim          : String,
        email        : String,
        password     : String,
        nomorWhatsapp: String
    ): Result<User> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("UID tidak ditemukan")

            val newUser = User(
                uid           = uid,
                namaLengkap   = namaLengkap,
                nim           = nim,
                email         = email,
                nomorWhatsapp = nomorWhatsapp,
                peran         = "mahasiswa"
            )

            usersCollection.document(uid).set(newUser.toMap()).await()
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        auth.signOut()
    }

    override fun getCurrentUserId(): String? {
        return auth.currentUser?.uid
    }
}