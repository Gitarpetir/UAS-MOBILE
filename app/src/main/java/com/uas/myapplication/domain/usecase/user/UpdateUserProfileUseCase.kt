package com.uas.myapplication.domain.usecase.user

import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.UserRepository

/**
 * Use Case: Memperbarui data profil pengguna.
 * Digunakan di Halaman Lengkapi Profil (setelah Google Sign-In)
 * dan di Halaman Profil.
 */
class UpdateUserProfileUseCase(
    private val userRepository: UserRepository
) {
    suspend operator fun invoke(
        uid: String,
        namaLengkap: String,
        nim: String,
        nomorWhatsapp: String
    ): Result<Unit> {
        return userRepository.updateUser(
            uid = uid,
            namaLengkap = namaLengkap,
            nim = nim,
            nomorWhatsapp = nomorWhatsapp
        )
    }

    /**
     * Overload: Simpan user baru (digunakan saat Google Sign-In user baru).
     */
    suspend fun saveNewUser(user: User): Result<Unit> {
        return userRepository.saveUser(user)
    }
}
