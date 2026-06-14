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
    val showDialogTemukan: Boolean   = false,
    val showDialogMilik  : Boolean   = false,
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

    fun onTombolAksiClick() {
        val status = _uiState.value.laporan?.statusBarang ?: return
        when (status) {
            StatusBarang.HILANG    -> _uiState.update { it.copy(showDialogTemukan = true) }
            StatusBarang.DITEMUKAN -> _uiState.update { it.copy(showDialogMilik = true) }
            StatusBarang.DIKLAIM -> { /* Tidak ada aksi untuk user */ }
            StatusBarang.SELESAI   -> { /* Tidak ada aksi */ }
        }
    }

    fun onBatalDialogTemukan() {
        _uiState.update { it.copy(showDialogTemukan = false) }
    }

    fun onBatalDialogMilik() {
        _uiState.update { it.copy(showDialogMilik = false) }
    }

    fun onKonfirmasiMilik() {
        val laporanId = _uiState.value.laporan?.id ?: return
        val currentUid = authRepository.getCurrentUserId() ?: return
        
        viewModelScope.launch {
            _uiState.update { it.copy(showDialogMilik = false, isLoading = true) }
            
            val user = getUserProfileUseCase(currentUid).getOrNull()
            
            konfirmasiTemuanUseCase(
                id = laporanId,
                status = StatusBarang.DIKLAIM,
                idPemilik = currentUid,
                namaPemilik = user?.namaLengkap ?: "",
                nimPemilik = user?.nim ?: "",
                whatsappPemilik = user?.nomorWhatsapp ?: ""
            )
            
            _uiState.update {
                it.copy(isLoading = false, aksiSuccess = true)
            }
        }
    }

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