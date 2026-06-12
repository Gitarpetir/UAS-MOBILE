package com.uas.myapplication.presentation.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.usecase.auth.RegisterUseCase
import com.uas.myapplication.domain.usecase.auth.LoginWithGoogleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegisterUiState(
    val namaLengkap              : String  = "",
    val nim                      : String  = "",
    val nomorWhatsapp            : String  = "",
    val email                    : String  = "",
    val password                 : String  = "",
    val konfirmasiPassword       : String  = "",
    val passwordVisible          : Boolean = false,
    val konfirmasiPasswordVisible: Boolean = false,
    val isLoading                : Boolean = false,
    val errorMessage             : String? = null,
    val passwordTidakSama        : Boolean = false,
    val registerSuccess          : Boolean = false,
    val googleSuccess            : Boolean = false
)

class RegisterViewModel(
    private val registerUseCase: RegisterUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(RegisterUiState())
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    fun onNamaLengkapChange(value: String) {
        _uiState.update { it.copy(namaLengkap = value, errorMessage = null) }
    }

    fun onNimChange(value: String) {
        _uiState.update { it.copy(nim = value, errorMessage = null) }
    }

    fun onNomorWhatsappChange(value: String) {
        _uiState.update { it.copy(nomorWhatsapp = value, errorMessage = null) }
    }

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                password          = value,
                errorMessage      = null,
                passwordTidakSama = value != it.konfirmasiPassword &&
                        it.konfirmasiPassword.isNotEmpty()
            )
        }
    }

    fun onKonfirmasiPasswordChange(value: String) {
        _uiState.update {
            it.copy(
                konfirmasiPassword = value,
                errorMessage       = null,
                passwordTidakSama  = value != it.password && value.isNotEmpty()
            )
        }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun onToggleKonfirmasiPasswordVisibility() {
        _uiState.update { it.copy(konfirmasiPasswordVisible = !it.konfirmasiPasswordVisible) }
    }

    fun register() {
        val state = _uiState.value

        // Validasi semua field tidak kosong
        if (state.namaLengkap.isBlank() || state.nim.isBlank() ||
            state.nomorWhatsapp.isBlank() || state.email.isBlank() ||
            state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Semua field harus diisi") }
            return
        }

        // Validasi password minimal 6 karakter
        if (state.password.length < 6) {
            _uiState.update { it.copy(errorMessage = "Password minimal 6 karakter") }
            return
        }

        // Validasi password sama
        if (state.password != state.konfirmasiPassword) {
            _uiState.update { it.copy(
                errorMessage      = "Password dan konfirmasi password tidak sama",
                passwordTidakSama = true
            )}
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }

            val result = registerUseCase(
                namaLengkap   = state.namaLengkap.trim(),
                nim           = state.nim.trim(),
                email         = state.email.trim(),
                password      = state.password,
                nomorWhatsapp = state.nomorWhatsapp.trim()
            )

            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, registerSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = pesanErrorFirebase(error.message))
                    }
                }
            )
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginWithGoogleUseCase(idToken)
            result.fold(
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, googleSuccess = true) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = pesanErrorFirebase(error.message))
                    }
                }
            )
        }
    }

    fun resetRegisterSuccess() {
        _uiState.update { it.copy(registerSuccess = false, googleSuccess = false) }
    }

    private fun pesanErrorFirebase(message: String?): String {
        return when {
            message == null                     -> "Terjadi kesalahan, coba lagi"
            message.contains("email address")   -> "Email sudah terdaftar"
            message.contains("badly formatted") -> "Format email tidak valid"
            message.contains("network")         -> "Tidak ada koneksi internet"
            message.contains("weak-password")   -> "Password terlalu lemah"
            else                                -> "Pendaftaran gagal, coba lagi"
        }
    }
}