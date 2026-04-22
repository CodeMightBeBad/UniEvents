package com.unibo.unievents.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class LoginUiState(
    val matricola: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val errorMessage: String? = null
)

sealed class LoginResult {
    object Success : LoginResult()
    data class Error(val message: String) : LoginResult()
}

class LoginViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    private val _loginResult = MutableStateFlow<LoginResult?>(null)
    val loginResult: StateFlow<LoginResult?> = _loginResult.asStateFlow()

    fun updateMatricola(matricola: String) {
        val filtered = matricola.filter { it.isDigit() }
        val newMatricola = if (filtered.length <= 10) filtered else filtered.take(10)

        _uiState.value = _uiState.value.copy(
            matricola = newMatricola,
            isLoginEnabled = isFormValid(newMatricola, _uiState.value.password),
            errorMessage = null
        )
    }

    fun updatePassword(password: String) {
        _uiState.value = _uiState.value.copy(
            password = password,
            isLoginEnabled = isFormValid(_uiState.value.matricola, password),
            errorMessage = null
        )
    }

    fun togglePasswordVisibility() {
        _uiState.value = _uiState.value.copy(
            isPasswordVisible = !_uiState.value.isPasswordVisible
        )
    }

    private fun isFormValid(matricola: String, password: String): Boolean {
        return matricola.length == 10 && password.length >= 6
    }

    fun onLoginClick() {
        val currentState = _uiState.value

        if (!isFormValid(currentState.matricola, currentState.password)) {
            _uiState.value = currentState.copy(
                errorMessage = "Matricola (10 cifre) e password (min 6 caratteri) richieste"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            // Simulazione chiamata API
            val result = performLogin(currentState.matricola, currentState.password)

            _uiState.value = _uiState.value.copy(isLoading = false)
            _loginResult.value = result
        }
    }

    private suspend fun performLogin(matricola: String, password: String): LoginResult {
        // Qui andrebbe la chiamata API reale
        // Per ora simuliamo un delay e validazione base
        kotlinx.coroutines.delay(1500)

        return if (matricola == "0000123456" && password == "password") {
            LoginResult.Success
        } else {
            LoginResult.Error("Matricola o password non validi")
        }
    }

    fun onRegisterClick() {
        // Naviga alla schermata di registrazione
    }

    fun onForgotPasswordClick() {
        // Naviga al recupero password
    }

    fun onExploreWithoutLoginClick() {
        // Naviga alla home come ospite
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}