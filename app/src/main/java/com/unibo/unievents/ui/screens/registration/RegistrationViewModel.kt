package com.unibo.unievents.ui.screens.registration

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RegistrationState(
    val username: String = "",
    val badgeNumber: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",

    val showPassword: Boolean = false
)

data class RegistrationActions(
    val updateUsername: (String) -> Unit,
    val updateBadgeNumber: (String) -> Unit,
    val updateEmail: (String) -> Unit,
    val updatePassword: (String) -> Unit,
    val updatePasswordConfirm: (String) -> Unit,
    val toggleShowPassword: () -> Unit
)

class RegistrationViewModel : ViewModel() {
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
        }
    )
}