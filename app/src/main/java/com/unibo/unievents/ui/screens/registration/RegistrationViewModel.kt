package com.unibo.unievents.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegistrationViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state.asStateFlow()

    fun handleAction(action: RegistrationActions) {
        when (action) {
            is RegistrationActions.UpdateUsername -> updateUsername(action.value)
            is RegistrationActions.UpdateMatricola -> updateMatricola(action.value)
            is RegistrationActions.UpdateEmail -> updateEmail(action.value)
            is RegistrationActions.UpdatePassword -> updatePassword(action.value)
            is RegistrationActions.UpdateConfirmPassword -> updateConfirmPassword(action.value)
            is RegistrationActions.UpdateAcceptedTerms -> updateAcceptedTerms(action.value)
            is RegistrationActions.Register -> register()
            is RegistrationActions.ResetSuccess -> resetSuccess()
            is RegistrationActions.ClearGeneralError -> clearGeneralError()
        }
    }

    private fun updateUsername(value: String) {
        _state.update { it.copy(username = value) }
    }

    private fun updateMatricola(value: String) {
        // Solo numeri, max 10 caratteri
        val filtered = value.filter { it.isDigit() }.take(10)
        _state.update {
            it.copy(
                matricola = filtered,
                matricolaError = validateMatricola(filtered)
            )
        }
    }

    private fun updateEmail(value: String) {
        _state.update {
            it.copy(
                email = value,
                emailError = validateEmail(value)
            )
        }
    }

    private fun updatePassword(value: String) {
        _state.update {
            it.copy(
                password = value,
                passwordError = validatePassword(value)
            )
        }
        // Ricontrolla la conferma password se non è vuota
        val currentState = _state.value
        if (currentState.confirmPassword.isNotBlank()) {
            _state.update {
                it.copy(
                    confirmPasswordError = validateConfirmPassword(currentState.confirmPassword, value)
                )
            }
        }
    }

    private fun updateConfirmPassword(value: String) {
        _state.update {
            it.copy(
                confirmPassword = value,
                confirmPasswordError = validateConfirmPassword(value, it.password)
            )
        }
    }

    private fun updateAcceptedTerms(value: Boolean) {
        _state.update {
            it.copy(
                acceptedTerms = value,
                termsError = if (value) null else it.termsError
            )
        }
    }

    // Validazioni
    private fun validateMatricola(value: String): String? {
        return when {
            value.isEmpty() -> "La matricola è obbligatoria"
            value.length != 10 -> "Deve essere di 10 cifre"
            else -> null
        }
    }

    private fun validateEmail(value: String): String? {
        return when {
            value.isEmpty() -> "L'email è obbligatoria"
            !value.endsWith("@studio.unibo.it") -> "Deve terminare con @studio.unibo.it"
            else -> null
        }
    }

    private fun validatePassword(value: String): String? {
        return when {
            value.isEmpty() -> "La password è obbligatoria"
            value.length < 8 -> "Almeno 8 caratteri"
            !value.any { it.isUpperCase() } -> "Almeno una lettera maiuscola"
            !value.any { it.isLowerCase() } -> "Almeno una lettera minuscola"
            !value.any { it.isDigit() } -> "Almeno un numero"
            !value.any { it in "!@#$%^&*()_+-=[]{}|;:,.<>?/~" } -> "Almeno un carattere speciale"
            else -> null
        }
    }

    private fun validateConfirmPassword(confirm: String, password: String): String? {
        return when {
            confirm.isEmpty() -> "Conferma la password"
            confirm != password -> "Le password non corrispondono"
            else -> null
        }
    }

    private fun validateTerms(): String? {
        return if (!_state.value.acceptedTerms) {
            "Devi accettare i Termini di Servizio e la Privacy Policy"
        } else {
            null
        }
    }

    private fun validateForm(): Boolean {
        val currentState = _state.value

        // Aggiorna tutti gli errori
        val matricolaError = validateMatricola(currentState.matricola)
        val emailError = validateEmail(currentState.email)
        val passwordError = validatePassword(currentState.password)
        val confirmPasswordError = validateConfirmPassword(
            currentState.confirmPassword,
            currentState.password
        )
        val termsError = validateTerms()

        _state.update {
            it.copy(
                matricolaError = matricolaError,
                emailError = emailError,
                passwordError = passwordError,
                confirmPasswordError = confirmPasswordError,
                termsError = termsError
            )
        }

        return matricolaError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null &&
                termsError == null
    }

    private fun register() {
        if (!validateForm()) {
            return
        }

        _state.update { it.copy(isLoading = true, generalError = null) }

        viewModelScope.launch {
            delay(1500) // Simulazione chiamata API

            try {
                val success = performRegistration()
                if (success) {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            registrationSuccess = true
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            generalError = "Errore durante la registrazione. Riprova."
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        generalError = e.message ?: "Errore sconosciuto"
                    )
                }
            }
        }
    }

    private suspend fun performRegistration(): Boolean {
        // TODO: Implementare chiamata API reale
        return true
    }

    private fun resetSuccess() {
        _state.update { it.copy(registrationSuccess = false) }
    }

    private fun clearGeneralError() {
        _state.update { it.copy(generalError = null) }
    }
}