package com.unibo.unievents.ui.screens.profile

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.repositories.UserRepository
import com.unibo.unievents.utils.bitmapToByteArray
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileState(
    val email: String = "",
    val badgeNumber: String = "",
    val profilePicture: Bitmap? = null,

    val loading: Boolean = false,
    val loadingImage: Boolean = false,
    val editing: Boolean = false
)

data class ProfileActions(
    val toggleEdit: () -> Unit,
    val updateProfilePicture: (Bitmap) -> Unit
)

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {
    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    val actions = ProfileActions(
        toggleEdit = { _state.update { it.copy(editing = !state.value.editing) } },
        updateProfilePicture = { bitmap ->
            _state.update { it.copy(profilePicture = bitmap) }

            viewModelScope.launch {
                val imageBytes = bitmapToByteArray(state.value.profilePicture!!)
                repository.uploadProfilePicture(imageBytes)
            }
        }
    )

    init {
        fetchInformation()
    }

    private fun fetchInformation() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val userInfo = repository.getCurrentUser()

            _state.update { it.copy(
                email = userInfo.email,
                badgeNumber = userInfo.badgeNumber,
                loading = false
            )}

            _state.update { it.copy(loadingImage = true) }
            _state.update { it.copy(
                profilePicture = repository.downloadProfilePicture(),
                loadingImage = false
            )}
        }
    }
}