package com.uas.myapplication.domain.usecase.laporan

import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.repository.LaporanRepository

/**
 * Use Case: Mengedit laporan yang sudah ada.
 * Jika ada foto baru, upload ulang ke Cloudinary.
 */
class EditLaporanUseCase(
    private val laporanRepository: LaporanRepository
) {
    suspend operator fun invoke(laporan: Laporan, fotoUri: String?): Result<Unit> {
        return laporanRepository.editLaporan(laporan, fotoUri)
    }
}
