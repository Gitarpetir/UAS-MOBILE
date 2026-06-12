package com.uas.myapplication.data.remote.datasource

import com.uas.myapplication.data.remote.dto.LaporanDto
import kotlinx.coroutines.flow.Flow

/**
 * Kontrak untuk operasi data laporan di Firestore dan upload foto ke Cloudinary.
 * Memisahkan detail implementasi Firebase Firestore & OkHttp dari Repository.
 */
interface LaporanRemoteDataSource {

    /**
     * Mengambil semua laporan secara realtime (Flow dari Firestore Snapshot Listener).
     * Diurutkan berdasarkan waktu_dibuat descending.
     */
    fun getAllLaporan(): Flow<List<LaporanDto>>

    /**
     * Mengambil laporan milik pengguna tertentu secara realtime.
     */
    fun getLaporanByUser(uid: String): Flow<List<LaporanDto>>

    /**
     * Mengambil satu laporan berdasarkan ID dokumen.
     */
    suspend fun getLaporanById(id: String): LaporanDto

    /**
     * Menyimpan dokumen laporan baru ke Firestore.
     * @return ID dokumen yang baru dibuat.
     */
    suspend fun createLaporan(data: Map<String, Any>): String

    /**
     * Memperbarui dokumen laporan yang sudah ada di Firestore.
     */
    suspend fun updateLaporan(id: String, data: Map<String, Any>)

    /**
     * Menghapus dokumen laporan berdasarkan ID.
     */
    suspend fun deleteLaporan(id: String)

    /**
     * Memperbarui field tertentu pada dokumen laporan (partial update).
     */
    suspend fun updateLaporanFields(id: String, fields: Map<String, Any>)

    /**
     * Upload foto ke Cloudinary.
     * @param uriString URI lokal foto dari galeri/kamera.
     * @return URL foto yang sudah diupload (secure_url).
     */
    suspend fun uploadFoto(uriString: String): String
}
