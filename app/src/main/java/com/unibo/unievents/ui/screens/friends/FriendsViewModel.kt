package com.unibo.unievents.ui.screens.friends

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.User
import com.unibo.unievents.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FriendsState(
    val friends: List<User> = emptyList(),
    val pendingFriends: List<User> = emptyList(),
    val incomingRequests: List<User> = emptyList(),

    val loading: Boolean = false
)

data class FriendsActions(
    val acceptRequest: (User) -> Unit,
    val denyRequest: (User) -> Unit,
    val removeFriend: (User) -> Unit,
)

class FriendsViewModel(private val repository: UserRepository) : ViewModel() {
    private val _state = MutableStateFlow(FriendsState())
    val state = _state.asStateFlow()

    val actions = FriendsActions(
        acceptRequest = { user ->
            viewModelScope.launch {
                repository.acceptFriendRequest(user.id)
                fetchData()
            }
        },
        denyRequest = { user ->
            viewModelScope.launch {
                repository.denyFriendRequest(user.id)
                fetchData()
            }
        },
        removeFriend = { user ->
            viewModelScope.launch {
                repository.removeFriend(user.id)
                fetchData()
            }
        }
    )

    private fun fetchData() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val incomingRequests = repository.getIncomingRequests()
            val friends = repository.getFriends()

            _state.update { it.copy(
                friends = friends,
                incomingRequests = incomingRequests,
                loading = false
            )}
        }
    }

    init {
         fetchData()
    }
}