package com.uas.myapplication.domain.repository

import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import kotlinx.coroutines.flow.Flow

interface LaporanRepository {

    fun getAllLaporan(): Flow<List<Laporan>>

    fun getLaporanByUser(uid: String): Flow<List<Laporan>>

    suspend fun getLaporanById(id: String): Result<Laporan>

    suspend fun buatLaporan(laporan: Laporan, fotoUri: String?): Result<Unit>

    suspend fun editLaporan(laporan: Laporan, fotoUri: String?): Result<Unit>

    suspend fun hapusLaporan(id: String): Result<Unit>

    suspend fun ubahStatus(
        id: String,
        status: StatusBarang,
        idPenemu: String? = null,
        namaPenemu: String? = null,
        nimPenemu: String? = null,
        whatsappPenemu: String? = null,
        idPemilik: String? = null,
        namaPemilik: String? = null,
        nimPemilik: String? = null,
        whatsappPemilik: String? = null
    ): Result<Unit>
}