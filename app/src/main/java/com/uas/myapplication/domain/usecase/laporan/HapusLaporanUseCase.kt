package com.uas.myapplication.domain.usecase.laporan

import com.uas.myapplication.domain.repository.LaporanRepository

class HapusLaporanUseCase(
    private val laporanRepository: LaporanRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return laporanRepository.hapusLaporan(id)
    }
}
