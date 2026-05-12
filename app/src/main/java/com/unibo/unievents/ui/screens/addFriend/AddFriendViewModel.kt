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

    val loading: Boolean = false,
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
            _state.update { it.copy(userEmail = email) }
        },
        sendRequest = {
            viewModelScope.launch {
                repository.sendRequest(state.value.userEmail)
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
