package com.unibo.unievents.ui.screens.registration

data class RegistrationState(
    val username: String = "Mario Rossi",
    val matricola: String = "0000123456",
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val acceptedTerms: Boolean = false,
    val isLoading: Boolean = false,
    val registrationSuccess: Boolean = false,
    val generalError: String? = null,

    // Errori per campo
    val matricolaError: String? = null,
    val emailError: String? = null,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val termsError: String? = null
) {
    val isFormValid: Boolean
        get() = matricolaError == null &&
                emailError == null &&
                passwordError == null &&
                confirmPasswordError == null &&
                termsError == null &&
                username.isNotBlank() &&
                matricola.isNotBlank() &&
                email.isNotBlank() &&
                password.isNotBlank() &&
                confirmPassword.isNotBlank() &&
                acceptedTerms
}