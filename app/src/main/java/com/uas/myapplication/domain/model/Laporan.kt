package com.uas.myapplication.domain.model

/**
 * Model murni bisnis untuk data laporan barang hilang/ditemukan.
 * Tidak boleh mengandung dependency Firebase atau library apapun.
 */
data class Laporan(
    val id: String = "",

    val idPelapor: String = "",
    val namaPelapor: String = "",
    val emailPelapor: String = "",
    val nimPelapor: String = "",
    val whatsappPelapor: String = "",

    val namaBarang: String = "",
    val deskripsi: String = "",
    val lokasi: String = "",
    val tanggal: String = "",
    val statusBarang: StatusBarang = StatusBarang.HILANG,
    val fotoUrl: String = "",
    val idPenemu: String = "",
    val namaPenemu: String = "",
    val nimPenemu: String = "",
    val whatsappPenemu: String = "",
    val idPemilik: String = "",
    val namaPemilik: String = "",
    val nimPemilik: String = "",
    val whatsappPemilik: String = "",
    val waktuDibuat: Long = 0L,
    val jenisLaporan: JenisLaporan = JenisLaporan.HILANG

)

/**
 * Enum untuk status barang.
 * Menggunakan enum agar type-safe dan tidak bisa salah ketik.
 */
enum class StatusBarang {
    HILANG,     // Barang sedang dicari pemiliknya
    DITEMUKAN,  // Barang sudah ditemukan, menunggu diklaim
    DIKLAIM,    // Barang diklaim oleh pengguna (Barang Ini Milik Saya)
    SELESAI     // Barang sudah dikembalikan (Laporan ditutup oleh admin)
}