package com.theseed.app.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.theseed.app.data.remote.AuthApi
import com.theseed.app.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class AuthUiState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val isAuthenticated: Boolean = false
)

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val authApi: AuthApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState: StateFlow<AuthUiState> = _uiState

    fun signInWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            authRepository.signInWithEmail(email, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Sign in failed"
                    )
                }
        }
    }

    fun signUpWithEmail(email: String, password: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            authRepository.signUpWithEmail(email, password)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Sign up failed"
                    )
                }
        }
    }

    fun signInWithGoogle(idToken: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            authRepository.signInWithGoogle(idToken)
                .onSuccess {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        isAuthenticated = true
                    )
                }
                .onFailure { e ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = e.message ?: "Google sign in failed"
                    )
                }
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }

    private val _backendVerified = MutableStateFlow<String?>(null)
    val backendVerified: StateFlow<String?> = _backendVerified

    fun verifyWithBackend() {
        viewModelScope.launch {
            try {
                val token = authRepository.getIdToken()
                Log.d("AuthTest", "Token retrieved: ${token?.take(20)}...")

                if (token == null) {
                    _backendVerified.value = "No token — user not logged in"
                    return@launch
                }

                Log.d("AuthTest", "Making request to /auth/me")
                val response = authApi.getMe("Bearer $token")
                Log.d("AuthTest", "Response received: ${response.message}")
                _backendVerified.value = "✅ Backend verified: ${response.user.email}"

            } catch (e: Exception) {
                Log.e("AuthTest", "Full error: ${e.javaClass.name}: ${e.message}")
                _backendVerified.value = "❌ Rejected: ${e.message}"
            }
        }
    }
}