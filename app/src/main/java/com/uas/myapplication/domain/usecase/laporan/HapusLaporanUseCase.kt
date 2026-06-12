package com.uas.myapplication.domain.usecase.laporan

import com.uas.myapplication.domain.repository.LaporanRepository

/**
 * Use Case: Menghapus laporan berdasarkan ID.
 */
class HapusLaporanUseCase(
    private val laporanRepository: LaporanRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return laporanRepository.hapusLaporan(id)
    }
}
