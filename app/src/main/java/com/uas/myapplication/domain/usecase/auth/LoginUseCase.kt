package com.uas.myapplication.domain.usecase.auth

import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.AuthRepository

class LoginUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<User> {
        return authRepository.loginWithEmail(email, password)
    }
}
