package com.uas.myapplication.domain.usecase.auth

import com.uas.myapplication.domain.model.GoogleAuthResult
import com.uas.myapplication.domain.repository.AuthRepository

class LoginWithGoogleUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<GoogleAuthResult> {
        return authRepository.loginWithGoogle(idToken)
    }
}
