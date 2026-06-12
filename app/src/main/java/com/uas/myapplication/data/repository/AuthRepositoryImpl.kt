package com.uas.myapplication.data.repository

import com.uas.myapplication.data.remote.datasource.AuthRemoteDataSource
import com.uas.myapplication.data.remote.datasource.UserRemoteDataSource
import com.uas.myapplication.data.remote.dto.toMap
import com.uas.myapplication.domain.model.GoogleAuthResult
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.AuthRepository

/**
 * Implementasi AuthRepository.
 * Sekarang bergantung pada DataSource, bukan langsung ke Firebase SDK.
 */
class AuthRepositoryImpl(
    private val authRemoteDataSource: AuthRemoteDataSource,
    private val userRemoteDataSource: UserRemoteDataSource
) : AuthRepository {

    override suspend fun loginWithEmail(email: String, password: String): Result<User> {
        return try {
            val uid = authRemoteDataSource.loginWithEmail(email, password)
            val userDto = userRemoteDataSource.getUserById(uid)
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun loginWithGoogle(
        idToken: String
    ): Result<GoogleAuthResult> {
        return try {
            val (uid, isNewUser) = authRemoteDataSource.loginWithGoogle(idToken)

            if (!isNewUser) {
                // User sudah ada — ambil data dari Firestore
                try {
                    val userDto = userRemoteDataSource.getUserById(uid)
                    Result.success(
                        GoogleAuthResult(
                            user = userDto.toDomain(),
                            isNewUser = false
                        )
                    )
                } catch (e: Exception) {
                    // Jika data user belum ada di Firestore meskipun bukan new user
                    // (kasus edge: user Firebase Auth ada tapi dokumen Firestore belum dibuat)
                    Result.success(
                        GoogleAuthResult(
                            user = User(uid = uid, email = "", peran = "mahasiswa"),
                            isNewUser = true
                        )
                    )
                }
            } else {
                // User baru — belum ada di Firestore
                Result.success(
                    GoogleAuthResult(
                        user = User(uid = uid, peran = "mahasiswa"),
                        isNewUser = true
                    )
                )
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun registerWithEmail(
        namaLengkap: String,
        nim: String,
        email: String,
        password: String,
        nomorWhatsapp: String
    ): Result<User> {
        return try {
            val uid = authRemoteDataSource.registerWithEmail(email, password)

            val newUser = User(
                uid = uid,
                namaLengkap = namaLengkap,
                nim = nim,
                email = email,
                nomorWhatsapp = nomorWhatsapp,
                peran = "mahasiswa"
            )

            userRemoteDataSource.saveUser(uid, newUser.toMap())
            Result.success(newUser)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun logout() {
        authRemoteDataSource.logout()
    }

    override fun getCurrentUserId(): String? {
        return authRemoteDataSource.getCurrentUserId()
    }
}