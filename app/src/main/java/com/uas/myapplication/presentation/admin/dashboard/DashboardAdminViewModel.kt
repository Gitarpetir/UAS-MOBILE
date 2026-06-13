package com.uas.myapplication.presentation.admin.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.model.Weather
import com.uas.myapplication.domain.usecase.laporan.GetAllLaporanUseCase
import com.uas.myapplication.domain.usecase.weather.GetWeatherUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DashboardAdminUiState(
    val daftarLaporan: List<Laporan> = emptyList(),

    val jumlahHilang: Int = 0,
    val jumlahDitemukan: Int = 0,
    val jumlahSelesai: Int = 0,

    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val isOfflineMode: Boolean = false,
    
    val weather: Weather? = null,
    val isWeatherLoading: Boolean = true,
    val weatherError: String? = null
)

class DashboardAdminViewModel(
    private val getAllLaporanUseCase: GetAllLaporanUseCase,
    private val getWeatherUseCase: GetWeatherUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardAdminUiState())
    val uiState: StateFlow<DashboardAdminUiState> = _uiState.asStateFlow()

    private var networkJob: kotlinx.coroutines.Job? = null
    private var laporanJob: kotlinx.coroutines.Job? = null
    private var weatherJob: kotlinx.coroutines.Job? = null

    fun ambilCuaca() {
        weatherJob?.cancel()
        weatherJob = viewModelScope.launch {
            _uiState.update { it.copy(isWeatherLoading = true, weatherError = null) }
            val lat = -3.2964016084518573
            val lon = 114.5870438388464
            
            getWeatherUseCase(lat, lon).fold(
                onSuccess = { cuaca ->
                    _uiState.update { it.copy(weather = cuaca, isWeatherLoading = false, weatherError = null) }
                },
                onFailure = { err ->
                    _uiState.update { it.copy(isWeatherLoading = false, weatherError = err.message) }
                }
            )
        }
    }

    fun ambilLaporan(context: android.content.Context) {
        networkJob?.cancel()
        networkJob = viewModelScope.launch {
            com.uas.myapplication.data.util.NetworkMonitor(context).isConnected.collect { isConnected ->
                _uiState.update { it.copy(isOfflineMode = !isConnected) }
            }
        }

        laporanJob?.cancel()
        laporanJob = viewModelScope.launch {
            getAllLaporanUseCase(context).collect { daftarLaporan ->

                _uiState.update {
                    it.copy(
                        daftarLaporan = daftarLaporan,

                        jumlahHilang = daftarLaporan.count {
                            it.statusBarang == StatusBarang.HILANG
                        },

                        jumlahDitemukan = daftarLaporan.count {
                            it.statusBarang == StatusBarang.DITEMUKAN
                        },

                        jumlahSelesai = daftarLaporan.count {
                            it.statusBarang == StatusBarang.SELESAI
                        },

                        isLoading = false
                    )
                }
            }
        }
    }
}