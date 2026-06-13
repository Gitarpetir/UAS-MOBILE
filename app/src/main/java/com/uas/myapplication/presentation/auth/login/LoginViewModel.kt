package com.uas.myapplication.presentation.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.uas.myapplication.domain.usecase.auth.LoginUseCase
import com.uas.myapplication.domain.usecase.auth.LoginWithGoogleUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import com.uas.myapplication.presentation.util.parseFirebaseError

// =============================================
// STATE — semua kondisi UI Login
// =============================================
data class LoginUiState(
    val email               : String  = "",
    val password            : String  = "",
    val passwordVisible     : Boolean = false,
    val isLoading           : Boolean = false,
    val errorMessage        : String? = null,
    val loginSuccess        : Boolean = false,
    val needsCompleteProfile: Boolean = false,
    val isAdmin             : Boolean = false
)

class LoginViewModel(
    private val loginUseCase: LoginUseCase,
    private val loginWithGoogleUseCase: LoginWithGoogleUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onEmailChange(value: String) {
        _uiState.update { it.copy(email = value, errorMessage = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, errorMessage = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun loginWithEmail() {
        val state = _uiState.value
        if (state.email.isBlank() || state.password.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Email dan password tidak boleh kosong") }
            return
        }
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginUseCase(state.email.trim(), state.password)
            result.fold(
                onSuccess = { user ->
                    _uiState.update { it.copy(isLoading = false, loginSuccess = true, isAdmin = user.isAdmin()) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = parseFirebaseError(error.message)) }
                }
            )
        }
    }

    fun loginWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            val result = loginWithGoogleUseCase(idToken)
            result.fold(
                onSuccess = { result ->

                    if (result.isNewUser) {

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                needsCompleteProfile = true
                            )
                        }

                    } else {

                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                loginSuccess = true,
                                isAdmin = result.user.isAdmin()
                            )
                        }

                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = parseFirebaseError(error.message)) }
                }
            )
        }
    }

    fun resetLoginSuccess() {
        _uiState.update { it.copy(loginSuccess = false) }
    }

    fun resetNeedsCompleteProfile() {
        _uiState.update {
            it.copy(needsCompleteProfile = false)
        }
    }
}