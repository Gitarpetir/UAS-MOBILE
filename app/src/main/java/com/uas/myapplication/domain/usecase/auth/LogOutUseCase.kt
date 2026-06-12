package com.uas.myapplication.domain.usecase.auth

import com.uas.myapplication.domain.repository.AuthRepository

/**
 * Use Case: Logout dari sistem.
 * Tanggung jawab tunggal: mengatur proses logout.
 */
class LogOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
