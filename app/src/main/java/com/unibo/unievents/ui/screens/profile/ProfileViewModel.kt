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

    val actions = ProfileActions(
        updatePassword = { password ->
            _state.update { it.copy(oldPassword = password) }
        },
        updateNewPassword = { password ->
            _state.update { it.copy(newPassword = password) }
        },
        setProfilePicture = { bitmap ->
            viewModelScope.launch {
                _state.update { it.copy(
                    profilePicture = bitmap,
                    loadingImage = true
                )}

                repository.uploadProfile(bitmapToByteArray(bitmap))

                _state.update { it.copy(loadingImage = false) }
            }
        }
    )

    fun fetchInformation() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            val userInfo = repository.getCurrentUser()

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
        }

        viewModelScope.launch {
            _state.update { it.copy(loadingImage = true) }
            _state.update { it.copy(
                profilePicture = repository.downloadProfilePicture(),
                loadingImage = false
            )}
        }
    }

    init {
        fetchInformation()
    }
}