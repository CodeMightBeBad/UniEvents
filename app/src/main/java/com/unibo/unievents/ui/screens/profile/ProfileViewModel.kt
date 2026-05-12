package com.unibo.unievents.ui.screens.profile

import android.graphics.Bitmap
import android.net.Uri
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
    val profilePictureUrl: String = "",
    val profileImageBitmap: Bitmap? = null,
    val oldPassword: String = "",
    val newPassword: String = "",
    val isLoading: Boolean = false
)

data class ProfileActions(
    val updatePassword: (String) -> Unit,
    val updateNewPassword: (String) -> Unit,
    val uploadImage: (Bitmap) -> Unit,
    val setProfileImage: (Bitmap) -> Unit,
    val fetchInformation: () -> Unit
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
        setProfileImage = { bitmap ->
            _state.update { it.copy(profileImageBitmap = bitmap) }
        },
        uploadImage = { bitmap ->
            _state.update { it.copy(profileImageBitmap = bitmap) }
            val imageBytes = bitmapToByteArray(bitmap)
            viewModelScope.launch {
                try {
                    repository.uploadProfile(imageBytes)
                    fetchInformation()
                    _state.update { it.copy(profileImageBitmap = bitmap) }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        },
        fetchInformation = { fetchInformation() }
    )

    fun fetchInformation(preserveBitmap: Boolean = false) {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            val userInfo = repository.getCurrentUser()
            val currentBitmap = if (preserveBitmap) _state.value.profileImageBitmap else null
            _state.update {
                it.copy(
                    email = userInfo.email,
                    badgeNumber = userInfo.badgeNumber,
                    profilePictureUrl = userInfo.profilePicture ?: "",
                    profileImageBitmap = currentBitmap ?: it.profileImageBitmap,
                    isLoading = false
                )
            }
        }
    }

    init {
        fetchInformation()
    }
}