package com.uas.myapplication.domain.usecase.auth

import com.uas.myapplication.domain.model.GoogleAuthResult
import com.uas.myapplication.domain.repository.AuthRepository

/**
 * Use Case: Login menggunakan Google Sign-In.
 * Tanggung jawab tunggal: mengatur proses login Google.
 */
class LoginWithGoogleUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(idToken: String): Result<GoogleAuthResult> {
        return authRepository.loginWithGoogle(idToken)
    }
}
