package com.unibo.unievents.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.repositories.AuthRepository
import io.github.jan.supabase.auth.exception.AuthRestException
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

    val confirm: () -> Unit
)

class LoginViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    val actions = LoginActions(
        updateEmail = { newEmail ->
            _state.update { it.copy(email = newEmail) }
        },
        updatePassword = { newPassword ->
            _state.update { it.copy(password = newPassword) }
        },
        confirm = {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }

                val result = repository.login(
                    email = state.value.email,
                    password = state.value.password
                )

                result.onFailure { exception ->
                    _state.update { it.copy(isLoading = false, errorMessage = exception.message) }
                }
            }
        }
    )
}
