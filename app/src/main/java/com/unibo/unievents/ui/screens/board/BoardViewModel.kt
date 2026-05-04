package com.unibo.unievents.ui.screens.board

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.repositories.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BoardState(
    val events: List<Event>,
    val loading: Boolean = false
)

data class BoardActions(
    val approveEvent: (Event) -> Unit,
    val removeEvent: (Event) -> Unit
)

class BoardViewModel(private val repository: EventRepository) : ViewModel() {
    private val _state = MutableStateFlow(BoardState(emptyList()))
    val state = _state.asStateFlow()

    val actions = BoardActions(
        approveEvent = { event ->
            viewModelScope.launch {
                repository.approveEvent(event)
            }

            fetchEvents()
        },
        removeEvent = { event ->
            viewModelScope.launch {
                repository.deleteEvent(event)
            }

            fetchEvents()
        }
    )

    fun fetchEvents() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val eventList = repository.getPendingEvents()
            _state.update { it.copy(events = eventList, loading = false) }
        }
    }

    init {
        fetchEvents()
    }
}