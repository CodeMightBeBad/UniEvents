package com.unibo.unievents.ui.screens.profile

import androidx.lifecycle.ViewModel

data class ProfileState(
    val displayName: String = "",
    val matricola: String = "",
    val email: String = "",
    val avatarInitial: String = "",
    val level: Int = 0,
    val points: Int = 0,
    val pointsToNextLevel: Int = 100,
    val eventiPartecipati: Int = 0,
    val eventiCreati: Int = 0,
    val utentiSeguiti: Int = 0,
    val livelloRaggiunto: Int = 0,
    val nome: String = "",
    val oldPassword: String = "",
    val newPassword: String = ""
)

data class ProfileActions(
    val onSaveProfile: (newName: String) -> Unit = {},
    val onSavePassword: (old: String, new: String) -> Unit = { _, _ -> },
    val onChangePhoto: () -> Unit = {},
    val onUpdateNome: (String) -> Unit = {},
    val onUpdateOldPassword: (String) -> Unit = {},
    val onUpdateNewPassword: (String) -> Unit = {}
)

class ProfileViewModel : ViewModel() {

}