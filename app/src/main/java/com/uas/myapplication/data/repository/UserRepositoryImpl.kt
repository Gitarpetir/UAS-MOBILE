package com.uas.myapplication.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.uas.myapplication.data.remote.dto.UserDto
import com.uas.myapplication.data.remote.dto.toMap
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.UserRepository
import kotlinx.coroutines.tasks.await

class UserRepositoryImpl(
    private val firestore: FirebaseFirestore
) : UserRepository {

    private val usersCollection = firestore.collection("users")

    override suspend fun getUserById(uid: String): Result<User> {
        return try {
            val snapshot = usersCollection.document(uid).get().await()
            val userDto  = snapshot.toObject(UserDto::class.java)
                ?: throw Exception("Data pengguna tidak ditemukan")
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            usersCollection.document(user.uid).set(user.toMap()).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Update nama, NIM, dan nomor WhatsApp pengguna.
     * Digunakan di Halaman Lengkapi Profil setelah login Google.
     */
    override suspend fun updateUser(
        uid          : String,
        namaLengkap  : String,
        nim          : String,
        nomorWhatsapp: String
    ): Result<Unit> {
        return try {
            usersCollection.document(uid).update(
                mapOf(
                    "nama_lengkap"   to namaLengkap,
                    "nim"            to nim,
                    "nomor_whatsapp" to nomorWhatsapp
                )
            ).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}