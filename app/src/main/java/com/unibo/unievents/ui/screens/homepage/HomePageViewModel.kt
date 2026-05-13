package com.unibo.unievents.ui.screens.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.repositories.EventRepository
import com.unibo.unievents.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomePageState (
    val events: List<Event> = emptyList(),
    val joinedEvents: List<Event> = emptyList(),
    val loading: Boolean = false
)

data class HomePageActions (
    val joinEvent: (Int) -> Unit,
    val leaveEvent: (Int) -> Unit,
    val isEventJoined: (Int) -> Boolean
)

class HomePageViewModel(private val eventsRepo: EventRepository, private val userRepo: UserRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomePageState())
    val state = _state.asStateFlow()

    val actions = HomePageActions(
        joinEvent = { eventID ->
            viewModelScope.launch {
                userRepo.joinEvent(eventID)
                fetchEvents()
            }
        },
        leaveEvent = { eventID ->
            viewModelScope.launch {
                userRepo.leaveEvent(eventID)
                fetchEvents()
            }
        },
        isEventJoined = { eventID ->
            state.value.joinedEvents.any { event -> event.id == eventID}
        }
    )

    fun fetchEvents() {
        viewModelScope.launch {
            // _state.update { it.copy(loading = true) }

            val eventList = eventsRepo.getApprovedEvents()
            val joinedEventsList = userRepo.getJoinedEvents()

            _state.update { it.copy(
                events = eventList,
                joinedEvents = joinedEventsList
            )}

            // _state.update { it.copy(loading = false) }
        }
    }

    init {
        fetchEvents()
    }
}
