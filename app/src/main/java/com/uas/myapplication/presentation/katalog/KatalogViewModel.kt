package com.uas.myapplication.presentation.katalog

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

// Filter yang bisa dipilih pengguna
enum class FilterKatalog { SEMUA, HILANG, DITEMUKAN }

data class KatalogUiState(
    val semuaLaporan  : List<Laporan>  = emptyList(),
    val laporanFilter : List<Laporan>  = emptyList(),
    val filterAktif   : FilterKatalog  = FilterKatalog.SEMUA,
    val queryPencarian: String         = "",
    val isLoading     : Boolean        = true,
    val errorMessage  : String?        = null
)

class KatalogViewModel(
    private val getAllLaporanUseCase: GetAllLaporanUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(KatalogUiState())
    val uiState: StateFlow<KatalogUiState> = _uiState.asStateFlow()

    init {
        ambilSemuaLaporan()
    }

    private fun ambilSemuaLaporan() {
        viewModelScope.launch {
            getAllLaporanUseCase().collect { daftarLaporan ->
                _uiState.update {
                    it.copy(
                        semuaLaporan = daftarLaporan,
                        isLoading    = false
                    )
                }
                // Terapkan filter setelah data masuk
                terapkanFilter()
            }
        }
    }

    // Update query pencarian dan terapkan filter
    fun onQueryPencarianChange(query: String) {
        _uiState.update { it.copy(queryPencarian = query) }
        terapkanFilter()
    }

    // Ganti filter aktif
    fun onFilterChange(filter: FilterKatalog) {
        _uiState.update { it.copy(filterAktif = filter) }
        terapkanFilter()
    }

    // Terapkan filter status + pencarian teks sekaligus
    private fun terapkanFilter() {
        val state  = _uiState.value
        val query  = state.queryPencarian.lowercase().trim()

        val hasilFilter = state.semuaLaporan
            // Filter berdasarkan status
            .filter { laporan ->
                when (state.filterAktif) {
                    FilterKatalog.SEMUA     -> true
                    FilterKatalog.HILANG    -> laporan.statusBarang == StatusBarang.HILANG
                    FilterKatalog.DITEMUKAN -> laporan.statusBarang == StatusBarang.DITEMUKAN
                }
            }
            // Filter berdasarkan teks pencarian
            .filter { laporan ->
                if (query.isEmpty()) true
                else laporan.namaBarang.lowercase().contains(query) ||
                        laporan.deskripsi.lowercase().contains(query) ||
                        laporan.lokasi.lowercase().contains(query)
            }

        _uiState.update { it.copy(laporanFilter = hasilFilter) }
    }
}