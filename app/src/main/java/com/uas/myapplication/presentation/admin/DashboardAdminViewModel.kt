package com.uas.myapplication.presentation.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.repository.AuthRepository
import com.uas.myapplication.domain.repository.LaporanRepository
import com.uas.myapplication.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

// =============================================
// FILTER & SORT
// =============================================

enum class FilterAdmin { SEMUA, HILANG, DITEMUKAN, SELESAI }
enum class SortAdmin   { TERBARU, TERLAMA }

// =============================================
// STATE
// =============================================

data class DashboardAdminUiState(
    val semuaLaporan      : List<Laporan> = emptyList(),
    val laporanTerfilter  : List<Laporan> = emptyList(),
    val namaAdmin         : String        = "",
    val filterAktif       : FilterAdmin   = FilterAdmin.SEMUA,
    val sortAktif         : SortAdmin     = SortAdmin.TERBARU,
    val isLoading         : Boolean       = true,
    val errorMessage      : String?       = null,
    // Statistik ringkasan
    val totalHilang       : Int           = 0,
    val totalDitemukan    : Int           = 0,
    val totalSelesai      : Int           = 0
)

class DashboardAdminViewModel(
    private val laporanRepository: LaporanRepository,
    private val userRepository   : UserRepository,
    private val authRepository   : AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardAdminUiState())
    val uiState: StateFlow<DashboardAdminUiState> = _uiState.asStateFlow()

    init {
        muatNamaAdmin()
        observeSemuaLaporan()
    }

    // Muat nama admin dari Firestore
    private fun muatNamaAdmin() {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            userRepository.getUserById(uid).getOrNull()?.let { user ->
                _uiState.update { it.copy(namaAdmin = user.namaLengkap) }
            }
        }
    }

    // Observasi semua laporan secara realtime
    private fun observeSemuaLaporan() {
        viewModelScope.launch {
            laporanRepository.getAllLaporan().collect { list ->
                val totalHilang    = list.count { it.statusBarang == StatusBarang.HILANG    }
                val totalDitemukan = list.count { it.statusBarang == StatusBarang.DITEMUKAN }
                val totalSelesai   = list.count { it.statusBarang == StatusBarang.SELESAI   }

                _uiState.update {
                    it.copy(
                        semuaLaporan   = list,
                        totalHilang    = totalHilang,
                        totalDitemukan = totalDitemukan,
                        totalSelesai   = totalSelesai,
                        isLoading      = false
                    )
                }
                terapkanFilterDanSort()
            }
        }
    }

    // Terapkan filter & sort ke daftar yang ditampilkan
    private fun terapkanFilterDanSort() {
        _uiState.update { state ->
            val filtered = when (state.filterAktif) {
                FilterAdmin.SEMUA     -> state.semuaLaporan
                FilterAdmin.HILANG    -> state.semuaLaporan.filter { it.statusBarang == StatusBarang.HILANG    }
                FilterAdmin.DITEMUKAN -> state.semuaLaporan.filter { it.statusBarang == StatusBarang.DITEMUKAN }
                FilterAdmin.SELESAI   -> state.semuaLaporan.filter { it.statusBarang == StatusBarang.SELESAI   }
            }
            val sorted = when (state.sortAktif) {
                SortAdmin.TERBARU -> filtered.sortedByDescending { it.waktuDibuat }
                SortAdmin.TERLAMA -> filtered.sortedBy          { it.waktuDibuat }
            }
            state.copy(laporanTerfilter = sorted)
        }
    }

    fun onFilterPilih(filter: FilterAdmin) {
        _uiState.update { it.copy(filterAktif = filter) }
        terapkanFilterDanSort()
    }

    fun onSortPilih(sort: SortAdmin) {
        _uiState.update { it.copy(sortAktif = sort) }
        terapkanFilterDanSort()
    }

    fun onLogout() {
        viewModelScope.launch {
            authRepository.logout()
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Barang dianggap "lama tidak diambil" jika statusnya DITEMUKAN
     * dan sudah lebih dari 3 hari sejak dilaporkan.
     */
    fun isBarangLama(laporan: Laporan): Boolean {
        if (laporan.statusBarang != StatusBarang.DITEMUKAN) return false
        val tigaHariMs = 3 * 24 * 60 * 60 * 1000L
        return (System.currentTimeMillis() - laporan.waktuDibuat) > tigaHariMs
    }
}