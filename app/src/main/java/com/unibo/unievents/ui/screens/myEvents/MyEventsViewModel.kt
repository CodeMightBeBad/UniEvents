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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class MyEventsState(
    val createdEvents: List<Event> = emptyList(),
    val joinedEvents: List<Event> = emptyList(),
    val loading: Boolean = false,
    val isCreatedExpanded: Boolean = true,
    val isJoinedExpanded: Boolean = true
)

data class MyEventsActions(
    val toggleCreatedExpanded: (Boolean) -> Unit,
    val toggleJoinedExpanded: (Boolean) -> Unit
)

class MyEventsViewModel(
    private val eventsRepo: EventRepository,
    private val userRepo: UserRepository
) : ViewModel() {
    private val _state = MutableStateFlow(MyEventsState())
    val state = _state.asStateFlow()

    val actions = MyEventsActions(
        toggleCreatedExpanded = {
            _state.update { it.copy(isCreatedExpanded = !it.isCreatedExpanded) }
        },
        toggleJoinedExpanded = {
            _state.update { it.copy(isJoinedExpanded = !it.isJoinedExpanded) }
        }
    )

    private fun filterFutureEvents(events: List<Event>): List<Event> {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

        val filteredEvents = events.filter { event ->
            event.date >= today
        }

        return filteredEvents.sortedBy { event ->
            event.date
        }
    }

    private fun fetchData() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val ownEvents = userRepo.getOwnEvents()
            val joinedEvents = userRepo.getJoinedEvents()

            _state.update { it.copy(
                createdEvents = filterFutureEvents(ownEvents),
                joinedEvents = filterFutureEvents(joinedEvents),
                loading = false
            )}
        }
    }

    init {
        fetchData()
    }
}