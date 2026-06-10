package com.uas.myapplication.domain.repository

import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import kotlinx.coroutines.flow.Flow

/**
 * Kontrak untuk operasi data laporan barang di Firestore.
 */
interface LaporanRepository {

    /**
     * Mengambil semua laporan secara realtime (Flow).
     * Digunakan di Halaman Katalog dan Dashboard.
     */
    fun getAllLaporan(): Flow<List<Laporan>>

    /**
     * Mengambil laporan milik pengguna tertentu secara realtime.
     * Digunakan di Halaman Laporanku.
     */
    fun getLaporanByUser(uid: String): Flow<List<Laporan>>

    /**
     * Mengambil detail satu laporan berdasarkan ID.
     * Digunakan di Halaman Detail Barang.
     */
    suspend fun getLaporanById(id: String): Result<Laporan>

    /**
     * Membuat laporan baru.
     * Menerima fotoUri (path lokal) → diupload ke Cloudinary → URL disimpan ke Firestore.
     */
    suspend fun buatLaporan(laporan: Laporan, fotoUri: String?): Result<Unit>

    /**
     * Mengedit laporan yang sudah ada.
     */
    suspend fun editLaporan(laporan: Laporan, fotoUri: String?): Result<Unit>

    /**
     * Menghapus laporan berdasarkan ID.
     */
    suspend fun hapusLaporan(id: String): Result<Unit>

    /**
     * Mengubah status barang.
     * Digunakan saat tombol "Aku menemukan barang ini" ditekan.
     */
    suspend fun ubahStatus(
        id: String,
        status: StatusBarang,
        idPenemu: String = ""
    ): Result<Unit>
}