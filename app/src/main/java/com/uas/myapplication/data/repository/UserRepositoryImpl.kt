package com.uas.myapplication.data.repository

import com.uas.myapplication.data.remote.datasource.UserRemoteDataSource
import com.uas.myapplication.data.remote.dto.toMap
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userRemoteDataSource: UserRemoteDataSource
) : UserRepository {

    override suspend fun getUserById(uid: String): Result<User> {
        return try {
            val userDto = userRemoteDataSource.getUserById(uid)
            Result.success(userDto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveUser(user: User): Result<Unit> {
        return try {
            userRemoteDataSource.saveUser(user.uid, user.toMap())
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun updateUser(
        uid: String,
        namaLengkap: String,
        nim: String,
        nomorWhatsapp: String
    ): Result<Unit> {
        return try {
            userRemoteDataSource.updateUser(
                uid,
                mapOf(
                    "nama_lengkap" to namaLengkap,
                    "nim" to nim,
                    "nomor_whatsapp" to nomorWhatsapp
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}