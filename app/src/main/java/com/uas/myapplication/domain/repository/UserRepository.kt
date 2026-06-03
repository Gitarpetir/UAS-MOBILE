package com.uas.myapplication.domain.repository

import com.uas.myapplication.domain.model.User

interface UserRepository {

    suspend fun getUserById(uid: String): Result<User>

    suspend fun saveUser(user: User): Result<Unit>

    /**
     * Update profil — sekarang include nomorWhatsapp
     */
    suspend fun updateUser(
        uid           : String,
        namaLengkap   : String,
        nim           : String,
        nomorWhatsapp : String = ""
    ): Result<Unit>
}