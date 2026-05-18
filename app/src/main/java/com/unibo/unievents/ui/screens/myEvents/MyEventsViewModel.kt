package com.unibo.unievents.ui.screens.myEvents

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.repositories.EventRepository
import com.unibo.unievents.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyEventsState(
    val createdEvents: List<Event> = emptyList(),
    val joinedEvents: List<Event> = emptyList(),

    val loading: Boolean = false
)

data class MyEventsActions(
    val switchMode: () -> Unit
)

class MyEventsViewModel(
    private val eventsRepo: EventRepository, private val userRepo: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MyEventsState())
    val state = _state.asStateFlow()

    val actions = MyEventsActions(
        switchMode = {}
    )

    private fun fetchData() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val ownEvents = userRepo.getOwnEvents()
            val joinedEvents = userRepo.getJoinedEvents()

            _state.update { it.copy(
                createdEvents = ownEvents,
                joinedEvents = joinedEvents,
                loading = false
            )}
        }
    }

    init {
        //fetchData()
    }
}