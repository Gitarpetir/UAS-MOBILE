package com.uas.myapplication.domain.usecase.user

import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.UserRepository

/**
 * Use Case: Mengambil informasi profil pengguna berdasarkan UID.
 */
class GetUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(uid: String): Result<User> {
        return userRepository.getUserById(uid)
    }
}
