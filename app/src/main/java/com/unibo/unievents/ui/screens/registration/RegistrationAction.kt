package com.unibo.unievents.ui.screens.registration

sealed class RegistrationActions {
    data class UpdateUsername(val value: String) : RegistrationActions()
    data class UpdateMatricola(val value: String) : RegistrationActions()
    data class UpdateEmail(val value: String) : RegistrationActions()
    data class UpdatePassword(val value: String) : RegistrationActions()
    data class UpdateConfirmPassword(val value: String) : RegistrationActions()
    data class UpdateAcceptedTerms(val value: Boolean) : RegistrationActions()

    object Register : RegistrationActions()
    object ResetSuccess : RegistrationActions()
    object ClearGeneralError : RegistrationActions()
}