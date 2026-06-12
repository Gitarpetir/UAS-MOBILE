package com.uas.myapplication.presentation.auth.lengkapi_profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.AuthRepository
import com.uas.myapplication.domain.usecase.user.UpdateUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.google.firebase.auth.FirebaseAuth

// =============================================
// STATE
// =============================================
data class LengkapiProfilUiState(
    val namaLengkap  : String  = "",
    val nim          : String  = "",
    val nomorWhatsapp: String  = "",
    val isLoading    : Boolean = false,
    val errorMessage : String? = null,
    val simpanSuccess: Boolean = false
)

class LengkapiProfilViewModel(
    private val authRepository: AuthRepository,
    private val updateUserProfileUseCase: UpdateUserProfileUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LengkapiProfilUiState())
    val uiState: StateFlow<LengkapiProfilUiState> = _uiState.asStateFlow()

    // Dipanggil saat halaman pertama dibuka —
    // mengisi nama dari akun Google secara otomatis
    fun inisialisasiDariGoogle(namaGoogle: String) {
        _uiState.update { it.copy(namaLengkap = namaGoogle) }
    }

    fun onNamaLengkapChange(value: String) {
        _uiState.update { it.copy(namaLengkap = value, errorMessage = null) }
    }

    fun onNimChange(value: String) {
        _uiState.update { it.copy(nim = value, errorMessage = null) }
    }

    fun onNomorWhatsappChange(value: String) {
        _uiState.update { it.copy(nomorWhatsapp = value, errorMessage = null) }
    }

    // Simpan profil ke Firestore
    fun simpanProfil() {
        val state = _uiState.value

        if (state.namaLengkap.isBlank() || state.nim.isBlank() || state.nomorWhatsapp.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Semua field harus diisi") }
            return
        }

        val uid = authRepository.getCurrentUserId()
        if (uid == null) {
            _uiState.update { it.copy(errorMessage = "Sesi tidak valid, silakan login ulang") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val firebaseUser = FirebaseAuth.getInstance().currentUser

            val user = User(
                uid = uid,
                namaLengkap = state.namaLengkap.trim(),
                nim = state.nim.trim(),
                email = firebaseUser?.email ?: "",
                nomorWhatsapp = state.nomorWhatsapp.trim(),
                peran = "mahasiswa"
            )

            val result = updateUserProfileUseCase.saveNewUser(user)

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, simpanSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            isLoading    = false,
                            errorMessage = "Gagal menyimpan profil: ${error.message}"
                        )
                    }
                }
            )
        }
    }

    fun resetSimpanSuccess() {
        _uiState.update { it.copy(simpanSuccess = false) }
    }
}