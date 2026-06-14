package com.uas.myapplication.data.remote.dto

import com.uas.myapplication.domain.model.JenisLaporan
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang

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
    val jenis_laporan: String = "HILANG",
    val foto_url: String = "",
    val id_penemu: String = "",
    val nama_penemu: String = "",
    val nim_penemu: String = "",
    val whatsapp_penemu: String = "",
    val id_pemilik: String = "",
    val nama_pemilik: String = "",
    val nim_pemilik: String = "",
    val whatsapp_pemilik: String = "",
    val waktu_dibuat: Long = 0L

) {
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
            "DIKLAIM"   -> StatusBarang.DIKLAIM
            "SELESAI"   -> StatusBarang.SELESAI
            else        -> StatusBarang.HILANG
        },
        jenisLaporan = when (jenis_laporan) {
            "TEMUAN" -> JenisLaporan.TEMUAN
            else -> JenisLaporan.HILANG
        },
        fotoUrl         = foto_url,
        idPenemu = id_penemu,
        namaPenemu = nama_penemu,
        nimPenemu = nim_penemu,
        whatsappPenemu = whatsapp_penemu,
        idPemilik = id_pemilik,
        namaPemilik = nama_pemilik,
        nimPemilik = nim_pemilik,
        whatsappPemilik = whatsapp_pemilik,
        waktuDibuat     = waktu_dibuat
    )
}

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
    "jenis_laporan"    to jenisLaporan.name,
    "foto_url"         to fotoUrl,
    "id_penemu"        to idPenemu,
    "nama_penemu"     to namaPenemu,
    "nim_penemu"      to nimPenemu,
    "whatsapp_penemu" to whatsappPenemu,
    "id_pemilik"       to idPemilik,
    "nama_pemilik"     to namaPemilik,
    "nim_pemilik"      to nimPemilik,
    "whatsapp_pemilik" to whatsappPemilik,
    "waktu_dibuat"     to waktuDibuat
)