package com.uas.myapplication.domain.usecase.auth

import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.AuthRepository

/**
 * Use Case: Registrasi akun baru menggunakan Email & Password.
 * Tanggung jawab tunggal: mengatur proses pendaftaran.
 */
class RegisterUseCase(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(
        namaLengkap: String,
        nim: String,
        email: String,
        password: String,
        nomorWhatsapp: String
    ): Result<User> {
        return authRepository.registerWithEmail(
            namaLengkap = namaLengkap,
            nim = nim,
            email = email,
            password = password,
            nomorWhatsapp = nomorWhatsapp
        )
    }
}
