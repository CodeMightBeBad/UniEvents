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

    val points: Int = 0,
    val level: Int = 0,
    val nextLevelPoints: Int = 0,

    val showLevelUpDialog: Boolean = false,
    val previousLevel: Int = 0,

    val createdEvents: Int = 0,
    val joinedEvents: Int = 0,
    val friends: Int = 0,

    val loading: Boolean = false,
    val loadingImage: Boolean = false,
    val editing: Boolean = false
)

data class ProfileActions(
    val updatePassword: (String) -> Unit,
    val updateNewPassword: (String) -> Unit,
    val toggleEdit: () -> Unit,
    val updateProfilePicture: (Bitmap) -> Unit,
    val dismissLevelUpDialog: () -> Unit
)

class ProfileViewModel(private val repository: UserRepository) : ViewModel() {

    companion object {
        private const val POINTS_PER_EVENT = 10
        private const val POINTS_PER_LEVEL = 30
    }

    private val _state = MutableStateFlow(ProfileState())
    val state = _state.asStateFlow()

    val actions = ProfileActions(
        updatePassword = { password ->
            _state.update { it.copy(oldPassword = password) }
        },
        updateNewPassword = { password ->
            _state.update { it.copy(newPassword = password) }
        },
        toggleEdit = { _state.update { it.copy(editing = !state.value.editing) } },
        updateProfilePicture = { bitmap ->
            _state.update { it.copy(profilePicture = bitmap) }

            viewModelScope.launch {
                val imageBytes = bitmapToByteArray(state.value.profilePicture!!)
                repository.uploadProfilePicture(imageBytes)
            }
        },
        dismissLevelUpDialog = {
            _state.update { it.copy(showLevelUpDialog = false) }
        }
    )

    init {
        fetchInformation()
    }

    private fun fetchInformation() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val userInfo = repository.getCurrentUser()

            val createdEvents = repository.getOwnEvents().size
            val joinedEvents = repository.getJoinedEvents().size
            val friends = repository.getFriends().size

            val points = joinedEvents * POINTS_PER_EVENT
            val (newLevel, nextLevelPoints) = calculateLevel(points)

            val savedLevel = userInfo.score
            val didLevelUp = newLevel > savedLevel

            if (newLevel != savedLevel) {
                repository.updateUserInformation(score = newLevel)
            }

            _state.update { it.copy(
                email = userInfo.email,
                badgeNumber = userInfo.badgeNumber,
                createdEvents = createdEvents,
                joinedEvents = joinedEvents,
                friends = friends,
                points = points,
                level = newLevel,
                nextLevelPoints = nextLevelPoints,
                showLevelUpDialog = didLevelUp,
                previousLevel = savedLevel,
                loading = false
            )}

            _state.update { it.copy(loadingImage = true) }
            _state.update { it.copy(
                profilePicture = repository.downloadProfilePicture(),
                loadingImage = false
            )}
        }
    }

    private fun calculateLevel(points: Int): Pair<Int, Int> {
        val level = points / POINTS_PER_LEVEL
        val nextLevelPoints = (level + 1) * POINTS_PER_LEVEL
        return Pair(level, nextLevelPoints)
    }
}