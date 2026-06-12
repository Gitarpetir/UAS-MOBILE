package com.uas.myapplication.presentation.laporan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.model.JenisLaporan
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.repository.AuthRepository
import com.uas.myapplication.domain.usecase.laporan.BuatLaporanUseCase
import com.uas.myapplication.domain.usecase.laporan.EditLaporanUseCase
import com.uas.myapplication.domain.usecase.laporan.GetLaporanByIdUseCase
import com.uas.myapplication.domain.usecase.user.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BuatLaporanUiState(
    val statusBarang   : StatusBarang = StatusBarang.HILANG,
    val fotoUri        : String       = "",
    val fotoUrl        : String       = "",
    val namaBarang     : String       = "",
    val deskripsi      : String       = "",
    val lokasi         : String       = "",
    val tanggal        : String       = "",
    val isEditMode     : Boolean      = false,
    val laporanId      : String       = "",
    val isLoading      : Boolean      = false,
    val errorMessage   : String?      = null,
    val kirimSuccess   : Boolean      = false,
    val namaBarangError: Boolean      = false,
    val deskripsiError : Boolean      = false,
    val lokasiError    : Boolean      = false,
    val tanggalError   : Boolean      = false
)

class BuatLaporanViewModel(
    private val buatLaporanUseCase: BuatLaporanUseCase,
    private val editLaporanUseCase: EditLaporanUseCase,
    private val getLaporanByIdUseCase: GetLaporanByIdUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(BuatLaporanUiState())
    val uiState: StateFlow<BuatLaporanUiState> = _uiState.asStateFlow()

    fun inisialisasiUntukEdit(laporanId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val result = getLaporanByIdUseCase(laporanId)
            result.fold(
                onSuccess = { laporan ->
                    _uiState.update {
                        it.copy(
                            isEditMode   = true,
                            laporanId    = laporanId,
                            statusBarang = laporan.statusBarang,
                            fotoUrl      = laporan.fotoUrl,
                            namaBarang   = laporan.namaBarang,
                            deskripsi    = laporan.deskripsi,
                            lokasi       = laporan.lokasi,
                            tanggal      = laporan.tanggal,
                            isLoading    = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = "Gagal memuat data: ${error.message}") }
                }
            )
        }
    }

    fun onStatusBarangChange(status: StatusBarang) {
        _uiState.update { it.copy(statusBarang = status) }
    }

    fun onFotoUriChange(uri: String) {
        _uiState.update { it.copy(fotoUri = uri) }
    }

    fun onNamaBarangChange(value: String) {
        _uiState.update { it.copy(namaBarang = value, namaBarangError = false, errorMessage = null) }
    }

    fun onDeskripsiChange(value: String) {
        _uiState.update { it.copy(deskripsi = value, deskripsiError = false, errorMessage = null) }
    }

    fun onLokasiChange(value: String) {
        _uiState.update { it.copy(lokasi = value, lokasiError = false, errorMessage = null) }
    }

    fun onTanggalChange(value: String) {
        _uiState.update { it.copy(tanggal = value, tanggalError = false, errorMessage = null) }
    }

    fun kirimLaporan() {
        val state           = _uiState.value
        val namaKosong      = state.namaBarang.isBlank()
        val deskripsiKosong = state.deskripsi.isBlank()
        val lokasiKosong    = state.lokasi.isBlank()
        val tanggalKosong   = state.tanggal.isBlank()

        if (namaKosong || deskripsiKosong || lokasiKosong || tanggalKosong) {
            _uiState.update {
                it.copy(
                    namaBarangError = namaKosong,
                    deskripsiError  = deskripsiKosong,
                    lokasiError     = lokasiKosong,
                    tanggalError    = tanggalKosong,
                    errorMessage    = "Semua field wajib diisi"
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val uid = authRepository.getCurrentUserId() ?: run {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Sesi tidak valid") }
                return@launch
            }

            val userResult = getUserProfileUseCase(uid)
            val user = userResult.getOrNull() ?: run {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Data pengguna tidak ditemukan") }
                return@launch
            }

            val laporan = Laporan(
                id              = state.laporanId,
                idPelapor       = uid,
                namaPelapor     = user.namaLengkap,
                emailPelapor    = user.email,
                nimPelapor      = user.nim,
                whatsappPelapor = user.nomorWhatsapp,
                namaBarang      = state.namaBarang.trim(),
                deskripsi       = state.deskripsi.trim(),
                lokasi          = state.lokasi.trim(),
                tanggal         = state.tanggal,
                statusBarang    = state.statusBarang,
                jenisLaporan =
                    if (state.statusBarang == StatusBarang.DITEMUKAN)
                        JenisLaporan.TEMUAN
                    else
                        JenisLaporan.HILANG,
                fotoUrl         = state.fotoUrl
            )

            val result = if (state.isEditMode) {
                editLaporanUseCase(laporan = laporan, fotoUri = state.fotoUri.ifEmpty { null })
            } else {
                buatLaporanUseCase(laporan = laporan, fotoUri = state.fotoUri.ifEmpty { null })
            }

            result.fold(
                onSuccess = { _uiState.update { it.copy(isLoading = false, kirimSuccess = true) } },
                onFailure = { error -> _uiState.update { it.copy(isLoading = false, errorMessage = "Gagal mengirim laporan: ${error.message}") } }
            )
        }
    }

    fun resetKirimSuccess() {
        _uiState.update { it.copy(kirimSuccess = false) }
    }
}