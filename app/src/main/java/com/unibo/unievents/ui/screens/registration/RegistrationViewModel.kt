package com.unibo.unievents.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class RegistrationState(
    val badgeNumber: String = "",
    val email: String = "",
    val password: String = "",
    val passwordConfirm: String = "",

    val showPassword: Boolean = false,
    val showConfirmPassword: Boolean = false,
    val loading: Boolean = false,
    val errorMessage: String? = null,

    val usernameError: String? = null,
    val badgeNumberError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val passwordConfirmError: String? = null,

    val registrationSuccess: Boolean = false
)

data class RegistrationActions(
    val updateBadgeNumber: (String) -> Unit,
    val updateEmail: (String) -> Unit,
    val updatePassword: (String) -> Unit,
    val updatePasswordConfirm: (String) -> Unit,
    val toggleShowPassword: () -> Unit,
    val toggleShowConfirmPassword: () -> Unit,
    val confirm: () -> Unit
)

class RegistrationViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state = _state.asStateFlow()

    val actions = RegistrationActions(
        updateBadgeNumber = { newBadgeNumber ->
            val error = if (newBadgeNumber.isNotEmpty() && (newBadgeNumber.length != 10 || !newBadgeNumber.all { it.isDigit() }))
                "La matricola deve essere di 10 cifre" else null
            _state.update { it.copy(badgeNumber = newBadgeNumber, badgeNumberError = error) }
        },
        updateEmail = { newEmail ->
            val error = if (newEmail.isNotEmpty() && !newEmail.endsWith("@studio.unibo.it"))
                "L'email deve terminare con @studio.unibo.it" else null
            _state.update { it.copy(email = newEmail, emailError = error) }
        },
        updatePassword = { newPassword ->
            val error = validatePassword(newPassword)
            val confirmError = if (state.value.passwordConfirm.isNotEmpty() && state.value.passwordConfirm != newPassword)
                "Le password non coincidono" else null
            _state.update { it.copy(password = newPassword, passwordError = error, passwordConfirmError = confirmError) }
        },
        updatePasswordConfirm = { newPasswordConfirm ->
            val error = if (newPasswordConfirm.isNotEmpty() && newPasswordConfirm != state.value.password)
                "Le password non coincidono" else null
            _state.update { it.copy(passwordConfirm = newPasswordConfirm, passwordConfirmError = error) }
        },
        toggleShowPassword = {
            _state.update { it.copy(showPassword = !it.showPassword) }
        },
        toggleShowConfirmPassword = {
            _state.update { it.copy(showConfirmPassword = !it.showConfirmPassword)}
        },

        confirm = {
            val badgeNumberError = when {
                state.value.badgeNumber.isEmpty() -> "La matricola è obbligatoria"
                state.value.badgeNumber.length != 10 || !state.value.badgeNumber.all { it.isDigit() } -> "La matricola deve essere di 10 cifre"
                else -> null
            }
            val emailError = when {
                state.value.email.isEmpty() -> "L'email è obbligatoria"
                !state.value.email.endsWith("@studio.unibo.it") -> "L'email deve terminare con @studio.unibo.it"
                else -> null
            }
            val passwordError = validatePassword(state.value.password)
            val passwordConfirmError = when {
                state.value.passwordConfirm.isEmpty() -> "La conferma della password è obbligatoria"
                state.value.passwordConfirm != state.value.password -> "Le password non coincidono"
                else -> null
            }

            _state.update {
                it.copy(
                    badgeNumberError = badgeNumberError,
                    emailError = emailError,
                    passwordError = passwordError,
                    passwordConfirmError = passwordConfirmError
                )
            }

            val isValid = listOf(badgeNumberError, emailError, passwordError, passwordConfirmError).all { it == null }

            if (isValid) {
                viewModelScope.launch {
                    _state.update { it.copy(loading = true) }
                    try {
                        repository.register(
                            email = state.value.email,
                            password = state.value.password,
                            badgeNumber = state.value.badgeNumber
                        )
                        _state.update { it.copy(registrationSuccess = true)
                        }
                    } catch (_: Exception) {
                        _state.update { it.copy(loading = false) }
                    } finally {
                        _state.update { it.copy(loading = false) }
                    }
                }
            }
        }
    )
    private fun validatePassword(password: String): String? {
        if (password.isEmpty()) return "La password è richiesta "
        if (password.length < 8) return "La password deve contenere almeno 8 caratteri"
        if (!password.any { it.isUpperCase() }) return "La password deve contenere almeno un carattere maiuscolo"
        if (!password.any { it.isLowerCase() }) return "La password deve contenere almeno un carattere minuscolo"
        if (!password.any { it.isDigit() }) return "La password deve contenere almeno un numero"
        if (!password.any { !it.isLetterOrDigit() }) return "La password deve contenere almeno un carattere speciale"
        return null
    }


    fun checkValidity() : Boolean {
        if (state.value.password != state.value.passwordConfirm) return false
        if (!state.value.email.endsWith("@studio.unibo.it")) return false

        return true
    }
}