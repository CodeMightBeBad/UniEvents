package com.unibo.unievents.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.repositories.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

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
    val onUpdateNewPassword: (String) -> Unit = {},
    val logout: () -> Unit
)

class ProfileViewModel(private val repository: AuthRepository) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    val actions = ProfileActions(
        logout = {
            viewModelScope.launch {
                repository.logout()
            }
        }
    )
}