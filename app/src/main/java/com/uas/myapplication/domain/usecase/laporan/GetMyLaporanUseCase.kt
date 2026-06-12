package com.uas.myapplication.domain.usecase.laporan

import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.repository.LaporanRepository
import kotlinx.coroutines.flow.Flow

/**
 * Use Case: Mengambil seluruh laporan milik pengguna yang sedang login.
 * Digunakan di halaman "Laporanku".
 */
class GetMyLaporanUseCase(
    private val laporanRepository: LaporanRepository
) {
    operator fun invoke(uid: String): Flow<List<Laporan>> {
        return laporanRepository.getLaporanByUser(uid)
    }
}
