package com.uas.myapplication.domain.usecase.laporan

import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.repository.LaporanRepository

class GetLaporanByIdUseCase(
    private val laporanRepository: LaporanRepository
) {
    suspend operator fun invoke(id: String): Result<Laporan> {
        return laporanRepository.getLaporanById(id)
    }
}
