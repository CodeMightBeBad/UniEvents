package com.unibo.unievents.ui.screens.addFriend

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.User
import com.unibo.unievents.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AddFriendState(
    val pendingRequests: List<User> = emptyList(),

    val userEmail: String = "",
    val emailError: String = "",

    val loading: Boolean = false,
    val userMailError: Boolean = false,
    val isEmailValid: Boolean = true
)

data class AddFriendsActions(
    val updateEmail: (String) -> Unit,
    val sendRequest: () -> Unit
)

class AddFriendViewModel(private val repository: UserRepository) : ViewModel() {
    private val _state = MutableStateFlow(AddFriendState())
    val state = _state.asStateFlow()

    val actions = AddFriendsActions(
        updateEmail = { email ->
            if (state.value.userMailError) {
                _state.update { it.copy(userMailError = false) }
            }

            _state.update { it.copy(userEmail = email) }
        },
        sendRequest = {
            viewModelScope.launch {
                _state.update { it.copy(loading = true) }
                val user = repository.getUserByEmail(state.value.userEmail)

                if (user == null) {
                   _state.update { it.copy(emailError = "User not found", userMailError = true) }
                } else if (repository.isFriend(user.id)){
                    _state.update { it.copy(emailError = "User is already in your friends list", userMailError = true) }
                } else {
                    repository.sendRequest(user.id)
                    fetchData()
                }

                _state.update { it.copy(loading = false) }
            }
        }
    )

    private fun fetchData() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val pending = repository.getPendingRequests()

            _state.update { it.copy(pendingRequests = pending, loading = false) }
        }
    }

    init {
        fetchData()
    }
}
