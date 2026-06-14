package com.uas.myapplication.data.remote.datasource

import com.uas.myapplication.data.remote.dto.UserDto

interface UserRemoteDataSource {

    suspend fun getUserById(uid: String): UserDto

    suspend fun saveUser(uid: String, data: Map<String, Any>)

    suspend fun updateUser(uid: String, fields: Map<String, Any>)
}
