package com.uas.myapplication.presentation.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.repository.LaporanRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardUiState(
    val daftarLaporan  : List<Laporan> = emptyList(),
    val jumlahHilang   : Int           = 0,
    val jumlahDitemukan: Int           = 0,
    val isLoading      : Boolean       = true,
    val errorMessage   : String?       = null
)

class DashboardViewModel(
    private val laporanRepository: LaporanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        // Mulai observasi data saat ViewModel pertama dibuat
        ambilLaporan()
    }

    private fun ambilLaporan() {
        viewModelScope.launch {
            // Flow realtime — otomatis update saat data di Firestore berubah
            laporanRepository.getAllLaporan().collect { daftarLaporan ->
                _uiState.update {
                    it.copy(
                        daftarLaporan   = daftarLaporan,
                        jumlahHilang    = daftarLaporan.count { l ->
                            l.statusBarang == StatusBarang.HILANG
                        },
                        jumlahDitemukan = daftarLaporan.count { l ->
                            l.statusBarang == StatusBarang.DITEMUKAN
                        },
                        isLoading       = false
                    )
                }
            }
        }
    }
}