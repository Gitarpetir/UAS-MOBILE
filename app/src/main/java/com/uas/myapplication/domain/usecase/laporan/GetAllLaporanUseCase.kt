package com.uas.myapplication.domain.usecase.laporan

import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.repository.LaporanRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use Case: Mengambil seluruh daftar laporan secara realtime.
 * Digunakan di halaman Katalog dan Dashboard.
 */
class GetAllLaporanUseCase(
    private val laporanRepository: LaporanRepository
) {
    operator fun invoke(context: android.content.Context): Flow<List<Laporan>> {
        return laporanRepository.getAllLaporan(context)
    }
}
