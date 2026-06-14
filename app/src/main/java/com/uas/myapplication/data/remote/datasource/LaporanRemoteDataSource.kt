package com.uas.myapplication.data.remote.datasource

import com.uas.myapplication.data.remote.dto.LaporanDto
import kotlinx.coroutines.flow.Flow

interface LaporanRemoteDataSource {

    fun getAllLaporan(): Flow<List<LaporanDto>>

    fun getLaporanByUser(uid: String): Flow<List<LaporanDto>>

    suspend fun getLaporanById(id: String): LaporanDto

    suspend fun createLaporan(data: Map<String, Any>): String

    suspend fun updateLaporan(id: String, data: Map<String, Any>)

    suspend fun deleteLaporan(id: String)

    suspend fun updateLaporanFields(id: String, fields: Map<String, Any>)

    suspend fun uploadFoto(uriString: String): String
}
