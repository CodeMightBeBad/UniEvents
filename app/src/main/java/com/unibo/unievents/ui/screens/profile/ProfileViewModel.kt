package com.unibo.unievents.ui.screens.profile

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.repositories.UserRepository
import com.unibo.unievents.utils.bitmapToByteArray
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileState(
    val email: String = "",
    val badgeNumber: String = "",
    val profilePicture: Bitmap? = null,
    val oldPassword: String = "",
    val newPassword: String = "",

    val createdEvents: Int = 0,
    val joinedEvents: Int = 0,
    val friends: Int = 0,

    val loadingImage: Boolean = false,
    val loading: Boolean = false
)

data class ProfileActions(
    val updatePassword: (String) -> Unit,
    val updateNewPassword: (String) -> Unit,
    val setProfilePicture: (Bitmap) -> Unit
)

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    // Flag per sapere se l'utente è caricato
    private val _isUserLoaded = MutableStateFlow(false)

    // Stato temporaneo per la foto durante l'editing
    private val _tempProfilePicture = MutableStateFlow<Bitmap?>(null)
    val tempProfilePicture = _tempProfilePicture.asStateFlow()

    val actions = ProfileActions(
        updatePassword = { password ->
            _state.update { it.copy(oldPassword = password) }
        },
        updateNewPassword = { password ->
            _state.update { it.copy(newPassword = password) }
        },
        setProfilePicture = { bitmap ->
            // Durante l'editing, salva temporaneamente la foto
            _tempProfilePicture.value = bitmap
            // Aggiorna anche lo stato principale per mostrarla subito nell'anteprima
            _state.update { it.copy(profilePicture = bitmap) }
        }
    )

    // Nuova funzione per salvare tutte le modifiche
    fun saveChanges() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(loading = true) }

                // Se c'è una foto temporanea, caricala
                _tempProfilePicture.value?.let { bitmap ->
                    repository.uploadProfile(bitmapToByteArray(bitmap))
                    // Ricarica la foto salvata
                    val newPicture = repository.downloadProfilePicture()
                    _state.update { it.copy(profilePicture = newPicture) }
                    _tempProfilePicture.value = null // Pulisci la temp
                }

                // Qui puoi aggiungere la logica per salvare altri campi modifica
                // come la password, se implementata

                _state.update { it.copy(loading = false) }
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(loading = false) }
            }
        }
    }

    fun fetchInformation() {
        viewModelScope.launch {
            try {
                _state.update { it.copy(loading = true) }

                val userInfo = repository.getCurrentUser()
                _isUserLoaded.value = true

                val createdEvents = repository.getOwnEvents().size
                val joinedEvents = repository.getJoinedEvents().size
                val friends = repository.getFriends().size

                _state.update { it.copy(
                    email = userInfo.email,
                    badgeNumber = userInfo.badgeNumber,
                    createdEvents = createdEvents,
                    joinedEvents = joinedEvents,
                    friends = friends,
                    loading = false
                )}
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(loading = false) }
            }
        }

        // Carica foto separatamente
        viewModelScope.launch {
            _isUserLoaded.first { it }

            try {
                _state.update { it.copy(loadingImage = true) }
                val picture = repository.downloadProfilePicture()
                _state.update { it.copy(
                    profilePicture = picture,
                    loadingImage = false
                )}
            } catch (e: Exception) {
                e.printStackTrace()
                _state.update { it.copy(loadingImage = false) }
            }
        }
    }

    init {
        fetchInformation()
    }
}