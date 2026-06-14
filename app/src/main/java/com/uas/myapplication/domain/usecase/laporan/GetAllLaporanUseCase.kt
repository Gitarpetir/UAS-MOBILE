package com.uas.myapplication.domain.usecase.laporan

import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.repository.LaporanRepository
import kotlinx.coroutines.flow.Flow

class GetAllLaporanUseCase(
    private val laporanRepository: LaporanRepository
) {
    operator fun invoke(): Flow<List<Laporan>> {
        return laporanRepository.getAllLaporan()
    }
}
