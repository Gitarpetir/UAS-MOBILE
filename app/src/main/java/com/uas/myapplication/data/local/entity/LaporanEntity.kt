package com.uas.myapplication.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.uas.myapplication.domain.model.JenisLaporan
import com.uas.myapplication.domain.model.StatusBarang

@Entity(tableName = "laporan_cache")
data class LaporanEntity(
    @PrimaryKey
    val id: String,
    val namaBarang: String,
    val deskripsi: String,
    val lokasi: String,
    val tanggal: String,
    val status: StatusBarang,
    val namaPelapor: String,
    val idPelapor: String,
    val fotoUrl: String?,
    val waktuDibuat: Long,
    val jenisLaporan: JenisLaporan,
    val idPenemu: String,
    val namaPenemu: String,
    val lastUpdated: Long = System.currentTimeMillis()
)

fun LaporanEntity.toDomain(): com.uas.myapplication.domain.model.Laporan {
    return com.uas.myapplication.domain.model.Laporan(
        id = this.id,
        namaBarang = this.namaBarang,
        deskripsi = this.deskripsi,
        lokasi = this.lokasi,
        tanggal = this.tanggal,
        statusBarang = this.status,
        namaPelapor = this.namaPelapor,
        idPelapor = this.idPelapor,
        fotoUrl = this.fotoUrl ?: "",
        waktuDibuat = this.waktuDibuat,
        jenisLaporan = this.jenisLaporan,
        idPenemu = this.idPenemu,
        namaPenemu = this.namaPenemu
    )
}

fun com.uas.myapplication.domain.model.Laporan.toEntity(): LaporanEntity {
    return LaporanEntity(
        id = this.id,
        namaBarang = this.namaBarang,
        deskripsi = this.deskripsi,
        lokasi = this.lokasi,
        tanggal = this.tanggal,
        status = this.statusBarang,
        namaPelapor = this.namaPelapor,
        idPelapor = this.idPelapor,
        fotoUrl = this.fotoUrl,
        waktuDibuat = this.waktuDibuat,
        jenisLaporan = this.jenisLaporan,
        idPenemu = this.idPenemu,
        namaPenemu = this.namaPenemu
    )
}
