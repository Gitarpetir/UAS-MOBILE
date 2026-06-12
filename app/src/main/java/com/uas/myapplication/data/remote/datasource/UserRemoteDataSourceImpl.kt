package com.uas.myapplication.data.remote.datasource

import com.google.firebase.firestore.FirebaseFirestore
import com.uas.myapplication.data.remote.dto.UserDto
import kotlinx.coroutines.tasks.await

/**
 * Implementasi UserRemoteDataSource yang berinteraksi langsung
 * dengan Firebase Firestore koleksi "users".
 */
class UserRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore
) : UserRemoteDataSource {

    private val usersCollection = firestore.collection("users")

    override suspend fun getUserById(uid: String): UserDto {
        val snapshot = usersCollection.document(uid).get().await()
        return snapshot.toObject(UserDto::class.java)
            ?: throw Exception("Data pengguna tidak ditemukan")
    }

    override suspend fun saveUser(uid: String, data: Map<String, Any>) {
        usersCollection.document(uid).set(data).await()
    }

    override suspend fun updateUser(uid: String, fields: Map<String, Any>) {
        usersCollection.document(uid).update(fields).await()
    }
}
