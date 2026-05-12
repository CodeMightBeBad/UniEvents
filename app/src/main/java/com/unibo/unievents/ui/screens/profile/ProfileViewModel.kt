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
    val oldPassword: String = "",
    val newPassword: String = "",
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

    val actions = ProfileActions(
        updatePassword = { password ->
            _state.update { it.copy(oldPassword = password) }
        },
        updateNewPassword = { password ->
            _state.update { it.copy(newPassword = password) }
        },
        setProfilePicture = { bitmap ->
            viewModelScope.launch {
                _state.update { it.copy(loading = true) }
                repository.uploadProfile(bitmapToByteArray(bitmap))

                _state.update { it.copy(
                    profilePicture = repository.downloadProfilePicture(),
                    loading = false
                )}

                fetchInformation()
            }
        }
    )

    fun fetchInformation() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            val userInfo = repository.getCurrentUser()

            _state.update { it.copy(
                email = userInfo.email,
                badgeNumber = userInfo.badgeNumber,
                profilePicture = repository.downloadProfilePicture(),
                loading = false
            )}
        }
    }

    init {
        fetchInformation()
    }
}