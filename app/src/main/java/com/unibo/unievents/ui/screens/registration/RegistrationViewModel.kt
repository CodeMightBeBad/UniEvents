package com.unibo.unievents.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationState(
    val username: String = "",
    val badgeNumber: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",

    val showPassword: Boolean = false,
    val showError: Boolean = false,
    val loading: Boolean = false
)

data class RegistrationActions(
    val updateUsername: (String) -> Unit,
    val updateBadgeNumber: (String) -> Unit,
    val updateEmail: (String) -> Unit,
    val updatePassword: (String) -> Unit,
    val updatePasswordConfirm: (String) -> Unit,
    val toggleShowPassword: () -> Unit,
    val confirm: () -> Unit
)

class RegistrationViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    val actions = RegistrationActions(
        updateUsername = { newUsername ->
            _state.update { it.copy(username = newUsername) }
        },
        updateBadgeNumber = { newBadgeNumber ->
            _state.update { it.copy(badgeNumber = newBadgeNumber) }
        },
        updateEmail = { newEmail ->
            _state.update { it.copy(email = newEmail) }
        },
        updatePassword = {password ->
            _state.update { it.copy(password = password) }
        },
        updatePasswordConfirm = { newPassword ->
            _state.update { it.copy(passwordConfirm = newPassword) }
        },
        toggleShowPassword = {
            _state.update { it.copy(showPassword = !it.showPassword) }
        },
        confirm = {
            if (checkValidity()) {
                _state.update { it.copy(loading = true) }

                try {
                    viewModelScope.launch {
                        repository.register(
                            email = state.value.email,
                            password = state.value.password,
                            badgeNumber = state.value.badgeNumber,
                            username = state.value.username
                        )
                    }
                } finally {
                    _state.update { it.copy(loading = false) }
                }
            } else {
                _state.update { it.copy(showError = true) }
            }
        }
    )

    fun checkValidity() : Boolean {
        if (state.value.password != state.value.passwordConfirm) return false
        if (!state.value.email.endsWith("@studio.unibo.it")) return false

        return true
    }
}