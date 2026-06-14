package com.uas.myapplication.domain.usecase.laporan

import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.repository.LaporanRepository

class KonfirmasiTemuanUseCase(
    private val laporanRepository: LaporanRepository
) {
    suspend operator fun invoke(
        id: String,
        status: StatusBarang,
        idPenemu: String? = null,
        namaPenemu: String? = null,
        nimPenemu: String? = null,
        whatsappPenemu: String? = null,
        idPemilik: String? = null,
        namaPemilik: String? = null,
        nimPemilik: String? = null,
        whatsappPemilik: String? = null
    ): Result<Unit> {
        return laporanRepository.ubahStatus(
            id = id,
            status = status,
            idPenemu = idPenemu,
            namaPenemu = namaPenemu,
            nimPenemu = nimPenemu,
            whatsappPenemu = whatsappPenemu,
            idPemilik = idPemilik,
            namaPemilik = namaPemilik,
            nimPemilik = nimPemilik,
            whatsappPemilik = whatsappPemilik
        )
    }
}
