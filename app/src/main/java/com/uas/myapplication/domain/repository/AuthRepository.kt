package com.uas.myapplication.domain.repository

import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.model.GoogleAuthResult

interface AuthRepository {

    suspend fun loginWithEmail(email: String, password: String): Result<User>

    suspend fun loginWithGoogle(idToken: String): Result<GoogleAuthResult>

    /**
     * Daftar akun baru — sekarang include nomorWhatsapp
     */
    suspend fun registerWithEmail(
        namaLengkap   : String,
        nim           : String,
        email         : String,
        password      : String,
        nomorWhatsapp : String = ""
    ): Result<User>

    suspend fun logout()

    fun getCurrentUserId(): String?

    fun getCurrentUserEmail(): String?

    fun getCurrentUserDisplayName(): String?
}