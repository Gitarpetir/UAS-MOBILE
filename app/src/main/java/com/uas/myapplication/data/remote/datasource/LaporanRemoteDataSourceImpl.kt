package com.uas.myapplication.data.remote.datasource

import android.content.Context
import android.util.Log
import androidx.core.net.toUri
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.uas.myapplication.BuildConfig
import com.uas.myapplication.data.remote.dto.LaporanDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class LaporanRemoteDataSourceImpl(
    private val firestore: FirebaseFirestore,
    private val context: Context,
    private val cloudinaryCloudName: String = BuildConfig.CLOUDINARY_CLOUD_NAME,
    private val cloudinaryUploadPreset: String = BuildConfig.CLOUDINARY_UPLOAD_PRESET
) : LaporanRemoteDataSource {

    private val laporanCollection = firestore.collection("laporan")
    private val httpClient = OkHttpClient()

    override fun getAllLaporan(): Flow<List<LaporanDto>> = callbackFlow {
        val listener = laporanCollection
            .orderBy("waktu_dibuat", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(LaporanDto::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    override fun getLaporanByUser(uid: String): Flow<List<LaporanDto>> = callbackFlow {
        val listener = laporanCollection
            .whereEqualTo("id_pelapor", uid)
            .orderBy("waktu_dibuat", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }
                val list = snapshot?.documents?.mapNotNull { doc ->
                    doc.toObject(LaporanDto::class.java)?.copy(id = doc.id)
                } ?: emptyList()
                trySend(list)
            }
        awaitClose { listener.remove() }
    }

    override suspend fun getLaporanById(id: String): LaporanDto {
        val snapshot = laporanCollection.document(id).get().await()
        return snapshot.toObject(LaporanDto::class.java)?.copy(id = snapshot.id)
            ?: throw Exception("Laporan tidak ditemukan")
    }

    override suspend fun createLaporan(data: Map<String, Any>): String {
        val docRef = laporanCollection.document()
        val dataWithId = data.toMutableMap()
        dataWithId["id"] = docRef.id
        docRef.set(dataWithId).await()
        return docRef.id
    }

    override suspend fun updateLaporan(id: String, data: Map<String, Any>) {
        laporanCollection.document(id).set(data).await()
    }

    override suspend fun deleteLaporan(id: String) {
        laporanCollection.document(id).delete().await()
    }

    override suspend fun updateLaporanFields(id: String, fields: Map<String, Any>) {
        laporanCollection.document(id).update(fields).await()
    }

    override suspend fun uploadFoto(uriString: String): String =
        withContext(Dispatchers.IO) {
            Log.d(
                "CLOUDINARY_TEST",
                "cloud=$cloudinaryCloudName preset=$cloudinaryUploadPreset"
            )
            val uri = uriString.toUri()
            Log.d("CLOUDINARY_URI", uri.toString())

            val inputStream = context.contentResolver.openInputStream(uri)
                ?: throw Exception("Tidak bisa membuka file foto")
            val bytes = inputStream.readBytes()
            inputStream.close()

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

            val response = httpClient.newCall(request).execute()
            val responseBody = response.body?.string()
                ?: throw Exception("Response Cloudinary kosong")
            Log.d("CLOUDINARY_RESPONSE", responseBody)

            if (!response.isSuccessful) throw Exception("Upload gagal: $responseBody")

            val json = JSONObject(responseBody)
            json.getString("secure_url")
        }
}
