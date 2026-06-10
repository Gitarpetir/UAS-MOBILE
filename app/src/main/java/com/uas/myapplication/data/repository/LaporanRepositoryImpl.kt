package com.uas.myapplication.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.uas.myapplication.data.remote.dto.LaporanDto
import com.uas.myapplication.data.remote.dto.toMap
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.repository.LaporanRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import com.uas.myapplication.BuildConfig
import androidx.core.net.toUri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Implementasi LaporanRepository.
 * Menangani operasi Firestore dan upload foto ke Cloudinary.
 */
class LaporanRepositoryImpl(
    private val firestore: FirebaseFirestore,
    private val context: Context,
    // Isi dengan data dari dashboard Cloudinary kamu
    private val cloudinaryCloudName: String = BuildConfig.CLOUDINARY_CLOUD_NAME,
    private val cloudinaryUploadPreset: String = BuildConfig.CLOUDINARY_UPLOAD_PRESET
) : LaporanRepository {

    private val laporanCollection = firestore.collection("laporan")
    private val httpClient = OkHttpClient()

    /**
     * Mengambil semua laporan secara realtime menggunakan Flow.
     * Diurutkan dari yang terbaru (waktu_dibuat descending).
     */
    override fun getAllLaporan(): Flow<List<Laporan>> = callbackFlow {
        val listener = laporanCollection
            .orderBy("waktu_dibuat", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(LaporanDto::class.java)?.copy(id = doc.id)?.toDomain()
                } ?: emptyList()
                trySend(list)
            }
        // Hentikan listener saat Flow tidak lagi digunakan
        awaitClose { listener.remove() }
    }

    /**
     * Mengambil laporan milik pengguna tertentu secara realtime.
     */
    override fun getLaporanByUser(uid: String): Flow<List<Laporan>> = callbackFlow {
        val listener = laporanCollection
            .whereEqualTo("id_pelapor", uid)
            .orderBy("waktu_dibuat", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(LaporanDto::class.java)?.copy(id = doc.id)?.toDomain()
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    /**
     * Mengambil satu laporan berdasarkan ID dokumen.
     */
    override suspend fun getLaporanById(id: String): Result<Laporan> {
        return try {
            val snapshot = laporanCollection.document(id).get().await()
            val dto = snapshot.toObject(LaporanDto::class.java)?.copy(id = snapshot.id)
                ?: throw Exception("Laporan tidak ditemukan")
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
                uploadFotoKeCloudinary(fotoUri)
            } else ""

            // 2. Buat dokumen baru di Firestore dengan Auto-ID
            val docRef = laporanCollection.document()
            val laporanDenganFoto = laporan.copy(
                id          = docRef.id,
                fotoUrl     = fotoUrl,
                waktuDibuat = System.currentTimeMillis()
            )

            // 3. Simpan ke Firestore
            docRef.set(laporanDenganFoto.toMap()).await()
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
                uploadFotoKeCloudinary(fotoUri)
            } else laporan.fotoUrl

            val laporanUpdate = laporan.copy(fotoUrl = fotoUrl)
            laporanCollection.document(laporan.id).set(laporanUpdate.toMap()).await()
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
            laporanCollection.document(id).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Mengubah status barang (HILANG → DITEMUKAN, dll).
     */
    override suspend fun ubahStatus(id: String, status: StatusBarang): Result<Unit> {
        return try {
            laporanCollection.document(id).update("status_barang", status.name).await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Fungsi private untuk mengupload foto ke Cloudinary.
     * Menggunakan Unsigned Upload agar API Secret tidak perlu disertakan.
     *
     * @param uriString URI lokal foto dari galeri/kamera pengguna
     * @return URL foto yang sudah diupload ke Cloudinary
     */
    private suspend fun uploadFotoKeCloudinary(uriString: String): String =
        withContext(Dispatchers.IO){
            Log.d(
                "CLOUDINARY_TEST",
                "cloud=$cloudinaryCloudName preset=$cloudinaryUploadPreset"
            )
            val uri = uriString.toUri()
            Log.d(
                "CLOUDINARY_URI",
                uri.toString()
            )

            // Baca byte dari URI lokal
            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw Exception("Tidak bisa membuka file foto")
            val bytes = inputStream.readBytes()
            inputStream.close()

            // Siapkan request multipart ke Cloudinary
            val requestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    "file",
                    "foto_barang.jpg",
                    bytes.toRequestBody("image/*".toMediaTypeOrNull())
                )
                .addFormDataPart("upload_preset", cloudinaryUploadPreset)
                .build()

            val request = Request.Builder()
                .url("https://api.cloudinary.com/v1_1/$cloudinaryCloudName/image/upload")
                .post(requestBody)
                .build()

            // Jalankan request secara sinkron (sudah di coroutine, aman)
            val response = httpClient.newCall(request).execute()
            val responseBody = response.body?.string()
                ?: throw Exception("Response Cloudinary kosong")
            Log.d("CLOUDINARY_RESPONSE", responseBody)

            if (!response.isSuccessful) throw Exception("Upload gagal: $responseBody")

            // Ambil URL dari response JSON Cloudinary
            val json = JSONObject(responseBody)
            json.getString("secure_url")
        }
    }
