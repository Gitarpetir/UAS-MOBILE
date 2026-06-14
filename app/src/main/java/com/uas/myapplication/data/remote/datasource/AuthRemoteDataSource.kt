package com.uas.myapplication.data.remote.datasource

import com.uas.myapplication.data.remote.dto.UserDto
import com.uas.myapplication.domain.model.GoogleAuthResult
interface AuthRemoteDataSource {

    suspend fun loginWithEmail(email: String, password: String): String
    suspend fun loginWithGoogle(idToken: String): Pair<String, Boolean>
    suspend fun registerWithEmail(email: String, password: String): String
    suspend fun logout()
    fun getCurrentUserId(): String?
    fun getCurrentUserEmail(): String?
    fun getCurrentUserDisplayName(): String?
}
