package com.uas.myapplication.presentation.admin.katalog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.usecase.laporan.GetAllLaporanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class FilterAdmin {
    SEMUA,
    HILANG,
    DITEMUKAN,
    SELESAI
}

data class KatalogAdminUiState(
    val semuaLaporan: List<Laporan> = emptyList(),
    val laporanFilter: List<Laporan> = emptyList(),
    val filterAktif: FilterAdmin = FilterAdmin.SEMUA,
    val queryPencarian: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isOfflineMode: Boolean = false
)

class KatalogAdminViewModel(
    private val getAllLaporanUseCase: GetAllLaporanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KatalogAdminUiState())
    val uiState: StateFlow<KatalogAdminUiState> = _uiState.asStateFlow()

    private var networkJob: kotlinx.coroutines.Job? = null
    private var laporanJob: kotlinx.coroutines.Job? = null

    fun ambilSemuaLaporan(context: android.content.Context) {
        networkJob?.cancel()
        networkJob = viewModelScope.launch {
            com.uas.myapplication.data.util.NetworkMonitor(context).isConnected.collect { isConnected ->
                _uiState.update { it.copy(isOfflineMode = !isConnected) }
            }
        }

        laporanJob?.cancel()
        laporanJob = viewModelScope.launch {
            getAllLaporanUseCase().collect { daftarLaporan ->

                _uiState.update {
                    it.copy(
                        semuaLaporan = daftarLaporan,
                        isLoading = false
                    )
                }

                terapkanFilter()
            }
        }
    }

    fun onQueryPencarianChange(query: String) {
        _uiState.update {
            it.copy(queryPencarian = query)
        }
        terapkanFilter()
    }

    fun onFilterChange(filter: FilterAdmin) {
        _uiState.update {
            it.copy(filterAktif = filter)
        }
        terapkanFilter()
    }

    private fun terapkanFilter() {

        val state = _uiState.value
        val query = state.queryPencarian.lowercase().trim()

        val hasilFilter = state.semuaLaporan

            .filter { laporan ->
                when (state.filterAktif) {
                    FilterAdmin.SEMUA ->
                        true

                    FilterAdmin.HILANG ->
                        laporan.statusBarang == StatusBarang.HILANG

                    FilterAdmin.DITEMUKAN ->
                        laporan.statusBarang == StatusBarang.DITEMUKAN

                    FilterAdmin.SELESAI ->
                        laporan.statusBarang == StatusBarang.SELESAI
                }
            }

            .filter { laporan ->
                if (query.isEmpty()) {
                    true
                } else {
                    laporan.namaBarang.lowercase().contains(query) ||
                            laporan.deskripsi.lowercase().contains(query) ||
                            laporan.lokasi.lowercase().contains(query)
                }
            }

        _uiState.update {
            it.copy(
                laporanFilter = hasilFilter
            )
        }
    }
}