package com.unibo.unievents.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginState(
    val email: String = "",
    val password: String = "",

    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val isLoginEnabled: Boolean = false,
    val errorMessage: String? = null
)

data class LoginActions(
    val updateEmail: (String) -> Unit,
    val updatePassword: (String) -> Unit,
    val togglePasswordVisibility: () -> Unit,
    val confirm: () -> Unit
)

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    val actions = LoginActions(

        updateEmail = { newEmail ->
            _state.update { it.copy(email = newEmail, errorMessage = null) }
        },
        updatePassword = { newPassword ->
            _state.update { it.copy(password = newPassword, errorMessage = null) }
        },
        togglePasswordVisibility = {
            _state.update { it.copy(isPasswordVisible = !it.isPasswordVisible) }
        },
        confirm = {
            if (checkValidity()) {
                viewModelScope.launch {
                    _state.update { it.copy(isLoading = true) }

                    try {
                        val result = repository.login(
                            email = state.value.email,
                            password = state.value.password
                        )

                        result.onFailure { exception ->
                            _state.update { it.copy(errorMessage = exception.message) }
                        }
                    } catch (_: Exception) {

                    } finally {
                        _state.update { it.copy(isLoading = false) }
                    }
                }
            } else {
                _state.update { it.copy(errorMessage = "Email o password sbagliata") }
            }
        }
    )

    fun checkValidity(): Boolean {
        if (state.value.email.isBlank() || state.value.password.isBlank()) return false
        if (!state.value.email.endsWith("@studio.unibo.it")) return false
        if (state.value.password.length < 8) return  false

        return true
    }
}
