package com.uas.myapplication.domain.usecase.laporan

import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.repository.LaporanRepository

/**
 * Use Case: Membuat laporan baru.
 * Alur: Upload gambar (jika ada) → Simpan data laporan ke Firestore.
 */
class BuatLaporanUseCase(
    private val laporanRepository: LaporanRepository
) {
    suspend operator fun invoke(laporan: Laporan, fotoUri: String?): Result<Unit> {
        return laporanRepository.buatLaporan(laporan, fotoUri)
    }
}
