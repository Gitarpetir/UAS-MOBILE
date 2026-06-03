package com.uas.myapplication.presentation.laporanku

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.repository.AuthRepository
import com.uas.myapplication.domain.repository.LaporanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// Tab yang aktif
enum class TabLaporanku { BARANGMU, TEMUANMU }

data class LaporankuUiState(
    val semuaLaporan  : List<Laporan>  = emptyList(),
    val tabAktif      : TabLaporanku   = TabLaporanku.BARANGMU,
    val isLoading     : Boolean        = true,
    val errorMessage  : String?        = null,
    val showHapusDialog: Boolean       = false,
    val laporanYangDihapus: Laporan?   = null
)

class LaporankuViewModel(
    private val laporanRepository: LaporanRepository,
    private val authRepository   : AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaporankuUiState())
    val uiState: StateFlow<LaporankuUiState> = _uiState.asStateFlow()

    init {
        ambilLaporanku()
    }

    private fun ambilLaporanku() {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            laporanRepository.getLaporanByUser(uid).collect { daftarLaporan ->
                _uiState.update {
                    it.copy(
                        semuaLaporan = daftarLaporan,
                        isLoading    = false
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
        return when (state.tabAktif) {
            // Tab Barangmu — laporan yang statusnya HILANG (milik sendiri)
            TabLaporanku.BARANGMU  -> state.semuaLaporan.filter {
                it.statusBarang == StatusBarang.HILANG ||
                        it.statusBarang == StatusBarang.DITEMUKAN
            }
            // Tab Temuanmu — laporan barang temuan yang sudah DITEMUKAN/SELESAI
            TabLaporanku.TEMUANMU -> state.semuaLaporan.filter {
                it.statusBarang == StatusBarang.DITEMUKAN ||
                        it.statusBarang == StatusBarang.SELESAI
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
            laporanRepository.hapusLaporan(laporan.id)
        }
    }

    // Jumlah laporan sesuai tab aktif
    fun getJumlahLaporan(): Int = getLaporanByTab().size
}