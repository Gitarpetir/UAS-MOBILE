package com.uas.myapplication.domain.usecase.auth

import com.uas.myapplication.domain.repository.AuthRepository

class LogOutUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke() {
        authRepository.logout()
    }
}
