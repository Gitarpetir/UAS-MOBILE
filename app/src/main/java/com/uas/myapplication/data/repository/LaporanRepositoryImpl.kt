package com.uas.myapplication.data.repository

import com.uas.myapplication.data.remote.datasource.LaporanRemoteDataSource
import com.uas.myapplication.data.remote.dto.toMap
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.repository.LaporanRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import com.uas.myapplication.data.local.entity.toEntity
import com.uas.myapplication.data.local.entity.toDomain

/**
 * Implementasi LaporanRepository.
 * Sekarang bergantung pada LaporanRemoteDataSource, bukan langsung ke Firestore & OkHttp SDK.
 */
class LaporanRepositoryImpl(
    private val laporanRemoteDataSource: LaporanRemoteDataSource,
    private val laporanDao: com.uas.myapplication.data.local.dao.LaporanDao
) : LaporanRepository {

    /**
     * Mengambil semua laporan secara realtime menggunakan Flow.
     * DataSource mengembalikan DTO, Repository memetakan ke Domain Model.
     */
    /**
     * Mengambil semua laporan secara realtime menggunakan Flow.
     * DataSource mengembalikan DTO, Repository memetakan ke Domain Model.
     */
    override fun getAllLaporan(context: android.content.Context): Flow<List<Laporan>> {
        return if (com.uas.myapplication.data.util.NetworkHelper.isConnected(context)) {
            laporanRemoteDataSource.getAllLaporan().map { dtoList ->
                val domainList = dtoList.map { it.toDomain() }
                kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                    laporanDao.clearAll()
                    laporanDao.insertAll(domainList.map { it.toEntity() })
                }
                domainList
            }
        } else {
            laporanDao.getAllLaporan().map { entityList ->
                entityList.map { it.toDomain() }
            }
        }
    }

    /**
     * Mengambil laporan milik pengguna tertentu secara realtime.
     */
    override fun getLaporanByUser(uid: String, context: android.content.Context): Flow<List<Laporan>> {
        return if (com.uas.myapplication.data.util.NetworkHelper.isConnected(context)) {
            laporanRemoteDataSource.getLaporanByUser(uid).map { dtoList ->
                val domainList = dtoList.map { it.toDomain() }
                domainList
            }
        } else {
            laporanDao.getAllLaporan().map { entityList ->
                entityList.map { it.toDomain() }.filter { it.idPelapor == uid || it.idPenemu == uid }
            }
        }
    }

    /**
     * Mengambil satu laporan berdasarkan ID dokumen.
     */
    override suspend fun getLaporanById(id: String): Result<Laporan> {
        return try {
            val dto = laporanRemoteDataSource.getLaporanById(id)
            Result.success(dto.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Membuat laporan baru.
     * Jika ada foto, upload ke Cloudinary dulu, lalu simpan URL-nya ke Firestore.
     */
    override suspend fun buatLaporan(laporan: Laporan, fotoUri: String?): Result<Unit> {
        return try {
            // 1. Upload foto ke Cloudinary jika ada
            val fotoUrl = if (!fotoUri.isNullOrEmpty()) {
                laporanRemoteDataSource.uploadFoto(fotoUri)
            } else ""

            // 2. Buat laporan dengan foto URL dan waktu dibuat
            val laporanDenganFoto = laporan.copy(
                fotoUrl = fotoUrl,
                waktuDibuat = System.currentTimeMillis()
            )

            // 3. Simpan ke Firestore via DataSource
            laporanRemoteDataSource.createLaporan(laporanDenganFoto.toMap())
            
            // 4. Cache ke Room (tanpa nomor WA, mapping ke entity)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                laporanDao.insertLaporan(laporanDenganFoto.toEntity())
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mengedit laporan yang sudah ada.
     * Jika ada foto baru (fotoUri tidak kosong), upload ulang ke Cloudinary.
     */
    override suspend fun editLaporan(laporan: Laporan, fotoUri: String?): Result<Unit> {
        return try {
            // Upload foto baru jika ada, jika tidak pakai URL lama
            val fotoUrl = if (!fotoUri.isNullOrEmpty()) {
                laporanRemoteDataSource.uploadFoto(fotoUri)
            } else laporan.fotoUrl

            val laporanUpdate = laporan.copy(fotoUrl = fotoUrl)
            laporanRemoteDataSource.updateLaporan(laporan.id, laporanUpdate.toMap())
            
            // Cache update
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                laporanDao.insertLaporan(laporanUpdate.toEntity())
            }
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Menghapus laporan berdasarkan ID.
     */
    override suspend fun hapusLaporan(id: String): Result<Unit> {
        return try {
            laporanRemoteDataSource.deleteLaporan(id)
            kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
                laporanDao.deleteById(id)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mengubah status barang (HILANG → DITEMUKAN, dll).
     */
    override suspend fun ubahStatus(
        id: String,
        status: StatusBarang,
        idPenemu: String,
        namaPenemu: String,
        nimPenemu: String,
        whatsappPenemu: String
    ): Result<Unit> {
        return try {
            laporanRemoteDataSource.updateLaporanFields(
                id,
                mapOf(
                    "status_barang" to status.name,
                    "id_penemu" to idPenemu,
                    "nama_penemu" to namaPenemu,
                    "nim_penemu" to nimPenemu,
                    "whatsapp_penemu" to whatsappPenemu
                )
            )
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
