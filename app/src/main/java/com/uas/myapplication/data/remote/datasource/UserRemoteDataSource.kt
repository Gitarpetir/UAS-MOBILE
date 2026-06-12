package com.uas.myapplication.data.remote.datasource

import com.uas.myapplication.data.remote.dto.UserDto

/**
 * Kontrak untuk operasi data pengguna di Firestore koleksi "users".
 * Memisahkan detail implementasi Firestore SDK dari Repository.
 */
interface UserRemoteDataSource {

    /**
     * Mengambil data pengguna berdasarkan UID.
     * @return UserDto dari Firestore.
     */
    suspend fun getUserById(uid: String): UserDto

    /**
     * Menyimpan data pengguna baru ke Firestore.
     */
    suspend fun saveUser(uid: String, data: Map<String, Any>)

    /**
     * Memperbarui field tertentu pada dokumen pengguna.
     */
    suspend fun updateUser(uid: String, fields: Map<String, Any>)
}
