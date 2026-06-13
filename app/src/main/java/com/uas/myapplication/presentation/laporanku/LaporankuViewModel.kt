package com.uas.myapplication.presentation.laporanku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.model.JenisLaporan
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.repository.AuthRepository
import com.uas.myapplication.domain.usecase.laporan.GetAllLaporanUseCase
import com.uas.myapplication.domain.usecase.laporan.HapusLaporanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Tab yang aktif
enum class TabLaporanku {
    BARANGKU,
    TEMUANKU,
    KONTRIBUSI
}

data class LaporankuUiState(
    val semuaLaporan  : List<Laporan>  = emptyList(),
    val tabAktif      : TabLaporanku   = TabLaporanku.BARANGKU,
    val isLoading     : Boolean        = true,
    val errorMessage  : String?        = null,
    val isOfflineMode : Boolean        = false,
    val showHapusDialog: Boolean       = false,
    val laporanYangDihapus: Laporan?   = null
)

class LaporankuViewModel(
    private val getAllLaporanUseCase: GetAllLaporanUseCase,
    private val hapusLaporanUseCase: HapusLaporanUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaporankuUiState())
    val uiState: StateFlow<LaporankuUiState> = _uiState.asStateFlow()

    private var networkJob: kotlinx.coroutines.Job? = null
    private var laporanJob: kotlinx.coroutines.Job? = null

    fun loadData(context: android.content.Context) {
        networkJob?.cancel()
        networkJob = viewModelScope.launch {
            com.uas.myapplication.data.util.NetworkMonitor(context).isConnected.collect { isConnected ->
                _uiState.update { it.copy(isOfflineMode = !isConnected) }
            }
        }

        laporanJob?.cancel()
        laporanJob = viewModelScope.launch {

            getAllLaporanUseCase()
                .collect { daftarLaporan ->

                    _uiState.update {
                        it.copy(
                            semuaLaporan = daftarLaporan,
                            isLoading = false
                        )
                    }
                }
        }
    }

    fun onTabChange(tab: TabLaporanku) {
        _uiState.update { it.copy(tabAktif = tab) }
    }

    // Filter laporan berdasarkan tab aktif
    fun getLaporanByTab(): List<Laporan> {

        val state = _uiState.value

        val currentUid =
            authRepository.getCurrentUserId()
                ?: return emptyList()

        return when (state.tabAktif) {

            TabLaporanku.BARANGKU -> {

                state.semuaLaporan.filter {
                    it.idPelapor == currentUid &&
                            it.jenisLaporan == JenisLaporan.HILANG
                }
            }

            TabLaporanku.TEMUANKU -> {

                state.semuaLaporan.filter {
                    it.idPelapor == currentUid &&
                            it.jenisLaporan == JenisLaporan.TEMUAN
                }
            }

            TabLaporanku.KONTRIBUSI -> {

                state.semuaLaporan.filter {

                    (
                            it.idPelapor == currentUid &&
                                    it.jenisLaporan == JenisLaporan.TEMUAN
                            )

                            ||

                            (
                                    it.idPenemu == currentUid &&
                                            it.idPelapor != currentUid
                                    )
                }
            }
        }
    }

    // Tampilkan dialog konfirmasi hapus
    fun onHapusClick(laporan: Laporan) {
        _uiState.update { it.copy(showHapusDialog = true, laporanYangDihapus = laporan) }
    }

    // Batalkan hapus
    fun onBatalHapus() {
        _uiState.update { it.copy(showHapusDialog = false, laporanYangDihapus = null) }
    }

    // Konfirmasi hapus — benar-benar hapus dari Firestore
    fun onKonfirmasiHapus() {
        val laporan = _uiState.value.laporanYangDihapus ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(showHapusDialog = false, laporanYangDihapus = null) }
            hapusLaporanUseCase(laporan.id)
        }
    }

    // Jumlah laporan sesuai tab aktif
    fun getJumlahLaporan(): Int = getLaporanByTab().size

    fun getCurrentUserId(): String? {
        return authRepository.getCurrentUserId()
    }
}