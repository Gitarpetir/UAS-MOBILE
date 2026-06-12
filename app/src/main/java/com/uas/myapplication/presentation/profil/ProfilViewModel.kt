package com.uas.myapplication.presentation.profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.data.local.PreferensiManager
import com.uas.myapplication.domain.model.User
import com.uas.myapplication.domain.repository.AuthRepository
import com.uas.myapplication.domain.usecase.auth.LogOutUseCase
import com.uas.myapplication.domain.usecase.user.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfilUiState(
    val user              : User?   = null,
    val isDarkMode        : Boolean = false,
    val bahasa            : String  = "id",
    val isLoading         : Boolean = true,
    val showBahasaDialog  : Boolean = false,
    val logoutSuccess     : Boolean = false,
    val errorMessage      : String? = null
)

class ProfilViewModel(
    private val authRepository: AuthRepository,
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val logOutUseCase: LogOutUseCase,
    private val preferensiManager: PreferensiManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfilUiState())
    val uiState: StateFlow<ProfilUiState> = _uiState.asStateFlow()

    init {
        muatProfil()
        observePreferensi()
    }

    private fun muatProfil() {
        val uid = authRepository.getCurrentUserId() ?: return
        viewModelScope.launch {
            val result = getUserProfileUseCase(uid)
            result.fold(
                onSuccess = { user ->
                    _uiState.update { it.copy(user = user, isLoading = false) }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(isLoading = false, errorMessage = error.message)
                    }
                }
            )
        }
    }

    // Observasi perubahan preferensi secara realtime dari DataStore
    private fun observePreferensi() {
        viewModelScope.launch {
            preferensiManager.isDarkMode.collect { isDark ->
                _uiState.update { it.copy(isDarkMode = isDark) }
            }
        }
        viewModelScope.launch {
            preferensiManager.bahasa.collect { bahasa ->
                _uiState.update { it.copy(bahasa = bahasa) }
            }
        }
    }

    // Toggle dark mode — langsung tersimpan ke DataStore
    fun onDarkModeToggle(isDark: Boolean) {
        viewModelScope.launch {
            preferensiManager.setDarkMode(isDark)
        }
    }

    // Tampilkan dialog pilihan bahasa
    fun onBahasaClick() {
        _uiState.update { it.copy(showBahasaDialog = true) }
    }

    fun onBatalBahasaDialog() {
        _uiState.update { it.copy(showBahasaDialog = false) }
    }

    // Simpan pilihan bahasa
    fun onPilihBahasa(kodeBahasa: String) {
        viewModelScope.launch {
            preferensiManager.setBahasa(kodeBahasa)
            _uiState.update { it.copy(showBahasaDialog = false) }
        }
    }

    fun onLogout() {
        viewModelScope.launch {
            logOutUseCase()
            _uiState.update { it.copy(logoutSuccess = true) }
        }
    }

    fun resetLogoutSuccess() {
        _uiState.update { it.copy(logoutSuccess = false) }
    }
}