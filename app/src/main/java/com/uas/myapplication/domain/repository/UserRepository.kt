package com.uas.myapplication.domain.repository

import com.uas.myapplication.domain.model.User

/**
 * Kontrak untuk operasi data pengguna di Firestore.
 */
interface UserRepository {

    /**
     * Mengambil data profil pengguna berdasarkan UID.
     */
    suspend fun getUserById(uid: String): Result<User>

    /**
     * Menyimpan data profil pengguna baru ke Firestore.
     * Dipanggil setelah registrasi berhasil.
     */
    suspend fun saveUser(user: User): Result<Unit>

    /**
     * Memperbarui data profil pengguna (nama & NIM).
     * Digunakan di Halaman Lengkapi Profil (Google Sign-In).
     */
    suspend fun updateUser(uid: String, namaLengkap: String, nim: String): Result<Unit>
}