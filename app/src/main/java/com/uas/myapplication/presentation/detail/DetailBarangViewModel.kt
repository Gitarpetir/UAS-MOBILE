package com.uas.myapplication.presentation.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.model.Laporan
import com.uas.myapplication.domain.model.StatusBarang
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.AuthRepository
import com.uas.myapplication.domain.usecase.laporan.GetLaporanByIdUseCase
import com.uas.myapplication.domain.usecase.laporan.KonfirmasiTemuanUseCase
import com.uas.myapplication.domain.usecase.user.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailBarangUiState(
    val laporan          : Laporan?  = null,
    val currentUser      : User?     = null,
    val isAdmin          : Boolean   = false,
    val isLoading        : Boolean   = true,
    val errorMessage     : String?   = null,
    // Dialog "Aku Menemukan Barang Ini"
    val showDialogTemukan: Boolean   = false,
    // Dialog "Barang Ini Milik Saya"
    val showDialogMilik  : Boolean   = false,
    // Aksi berhasil — navigasi kembali
    val aksiSuccess      : Boolean   = false
)

class DetailBarangViewModel(
    private val getLaporanByIdUseCase: GetLaporanByIdUseCase,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val konfirmasiTemuanUseCase: KonfirmasiTemuanUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailBarangUiState())
    val uiState: StateFlow<DetailBarangUiState> = _uiState.asStateFlow()

    fun muatDetail(laporanId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }

            // Ambil detail laporan
            val laporanResult = getLaporanByIdUseCase(laporanId)
            laporanResult.fold(
                onSuccess = { laporan ->
                    _uiState.update { it.copy(laporan = laporan) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = "Gagal memuat detail: ${error.message}")
                    }
                    return@launch
                }
            )

            // Ambil data pengguna yang sedang login
            val uid = authRepository.getCurrentUserId()
            if (uid != null) {
                val userResult = getUserProfileUseCase(uid)
                userResult.getOrNull()?.let { user ->
                    _uiState.update {
                        it.copy(
                            currentUser = user,
                            isAdmin     = user.isAdmin(),
                            isLoading   = false
                        )
                    }
                }
            } else {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    // Tampilkan dialog sesuai status barang
    fun onTombolAksiClick() {
        val status = _uiState.value.laporan?.statusBarang ?: return
        when (status) {
            StatusBarang.HILANG    -> _uiState.update { it.copy(showDialogTemukan = true) }
            StatusBarang.DITEMUKAN -> _uiState.update { it.copy(showDialogMilik = true) }
            StatusBarang.SELESAI   -> { /* Tidak ada aksi */ }
        }
    }

    fun onBatalDialogTemukan() {
        _uiState.update { it.copy(showDialogTemukan = false) }
    }

    fun onBatalDialogMilik() {
        _uiState.update { it.copy(showDialogMilik = false) }
    }

    // Konfirmasi "Aku Menemukan Barang Ini" — ubah status HILANG → DITEMUKAN
    fun onKonfirmasiTemukan() {

        val laporanId =
            _uiState.value.laporan?.id ?: return

        val currentUid =
            authRepository.getCurrentUserId() ?: return

        viewModelScope.launch {

            _uiState.update {
                it.copy(
                    showDialogTemukan = false,
                    isLoading = true
                )
            }

            val user =
                getUserProfileUseCase(currentUid)
                    .getOrNull()

            konfirmasiTemuanUseCase(
                id = laporanId,
                status = StatusBarang.DITEMUKAN,
                idPenemu = currentUid,
                namaPenemu = user?.namaLengkap ?: "",
                nimPenemu = user?.nim ?: "",
                whatsappPenemu = user?.nomorWhatsapp ?: ""
            )

            _uiState.update {
                it.copy(
                    isLoading = false,
                    aksiSuccess = true
                )
            }
        }
    }

    // Admin — selesaikan laporan → ubah status menjadi SELESAI
    fun onSelesaikanLaporan() {
        val laporanId = _uiState.value.laporan?.id ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            konfirmasiTemuanUseCase(laporanId, StatusBarang.SELESAI)
            _uiState.update { it.copy(isLoading = false, aksiSuccess = true) }
        }
    }

    fun resetAksiSuccess() {
        _uiState.update { it.copy(aksiSuccess = false) }
    }
}