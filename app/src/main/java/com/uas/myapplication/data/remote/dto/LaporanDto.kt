package com.uas.myapplication.data.remote.dto

import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang

/**
 * DTO untuk koleksi `laporan` di Firestore.
 * Nama field menggunakan snake_case sesuai skema database.
 */
data class LaporanDto(
    val id: String = "",
    val id_pelapor: String = "",
    val nama_pelapor: String = "",
    val email_pelapor: String = "",
    val nim_pelapor: String = "",
    val whatsapp_pelapor: String = "",
    val nama_barang: String = "",
    val deskripsi: String = "",
    val lokasi: String = "",
    val tanggal: String = "",
    val status_barang: String = "HILANG",
    val foto_url: String = "",
    val waktu_dibuat: Long = 0L
) {
    /**
     * Mengubah DTO menjadi Domain Model (Laporan).
     */
    fun toDomain(): Laporan = Laporan(
        id              = id,
        idPelapor       = id_pelapor,
        namaPelapor     = nama_pelapor,
        emailPelapor    = email_pelapor,
        nimPelapor      = nim_pelapor,
        whatsappPelapor = whatsapp_pelapor,
        namaBarang      = nama_barang,
        deskripsi       = deskripsi,
        lokasi          = lokasi,
        tanggal         = tanggal,
        statusBarang    = when (status_barang) {
            "DITEMUKAN" -> StatusBarang.DITEMUKAN
            "SELESAI"   -> StatusBarang.SELESAI
            else        -> StatusBarang.HILANG
        },
        fotoUrl         = foto_url,
        waktuDibuat     = waktu_dibuat
    )
}

/**
 * Extension function untuk mengubah Domain Model Laporan menjadi Map
 * agar bisa disimpan ke Firestore.
 */
fun Laporan.toMap(): Map<String, Any> = mapOf(
    "id"               to id,
    "id_pelapor"       to idPelapor,
    "nama_pelapor"     to namaPelapor,
    "email_pelapor"    to emailPelapor,
    "nim_pelapor"      to nimPelapor,
    "whatsapp_pelapor" to whatsappPelapor,
    "nama_barang"      to namaBarang,
    "deskripsi"        to deskripsi,
    "lokasi"           to lokasi,
    "tanggal"          to tanggal,
    "status_barang"    to statusBarang.name,
    "foto_url"         to fotoUrl,
    "waktu_dibuat"     to waktuDibuat
)