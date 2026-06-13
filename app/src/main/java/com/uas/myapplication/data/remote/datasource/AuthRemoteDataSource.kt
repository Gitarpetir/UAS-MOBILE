package com.uas.myapplication.data.remote.datasource

import com.uas.myapplication.data.remote.dto.UserDto
import com.uas.myapplication.domain.model.GoogleAuthResult

/**
 * Kontrak untuk operasi autentikasi di Firebase Auth.
 * Memisahkan detail implementasi Firebase SDK dari Repository.
 */
interface AuthRemoteDataSource {

    /**
     * Login menggunakan email dan password.
     * @return UID pengguna yang berhasil login.
     */
    suspend fun loginWithEmail(email: String, password: String): String

    /**
     * Login menggunakan Google ID Token.
     * @return Pair(uid, isNewUser) — UID Firebase dan flag apakah user baru.
     */
    suspend fun loginWithGoogle(idToken: String): Pair<String, Boolean>

    /**
     * Mendaftarkan akun baru menggunakan email dan password.
     * @return UID pengguna yang baru didaftarkan.
     */
    suspend fun registerWithEmail(email: String, password: String): String

    /**
     * Logout dari Firebase Auth dan Google Sign-In.
     */
    suspend fun logout()

    /**
     * Mengambil UID pengguna yang sedang login.
     * @return UID atau null jika tidak ada session aktif.
     */
    fun getCurrentUserId(): String?

    fun getCurrentUserEmail(): String?

    fun getCurrentUserDisplayName(): String?
}
