package com.uas.myapplication.domain.model

/**
 * Model murni bisnis untuk data laporan barang hilang/ditemukan.
 * Tidak boleh mengandung dependency Firebase atau library apapun.
 */
data class Laporan(
    val id: String = "",

    // Data pelapor (denormalisasi — disimpan langsung di dokumen)
    val idPelapor: String = "",
    val namaPelapor: String = "",
    val emailPelapor: String = "",
    val nimPelapor: String = "",
    val whatsappPelapor: String = "",

    // Data barang
    val namaBarang: String = "",
    val deskripsi: String = "",
    val lokasi: String = "",
    val tanggal: String = "",
    val statusBarang: StatusBarang = StatusBarang.HILANG,
    val fotoUrl: String = "",
    val idPenemu: String = "",
    // Metadata
    val waktuDibuat: Long = 0L


)

/**
 * Enum untuk status barang.
 * Menggunakan enum agar type-safe dan tidak bisa salah ketik.
 */
enum class StatusBarang {
    HILANG,     // Barang sedang dicari pemiliknya
    DITEMUKAN,  // Barang sudah ditemukan, menunggu diklaim
    SELESAI     // Laporan sudah diselesaikan oleh admin
}