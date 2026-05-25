package com.unibo.unievents.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.repositories.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MapState (
    val events: List<Event> = emptyList(),
    val selectedEvent: Pair<Double, Double>? = null,

    val loading: Boolean = false
)

data class MapActions (
    val selectEvent: (Double, Double) -> Unit
)

class MapViewModel(private val repository: EventRepository): ViewModel() {
    private val _state = MutableStateFlow(MapState())
    val state = _state.asStateFlow()

    val actions = MapActions(
        selectEvent = { lat, long ->
            _state.update { it.copy(selectedEvent = Pair(lat, long)) }
        }
    )

    init {
        fetchEvents()
    }

    private fun fetchEvents() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val events = repository.getApprovedEvents()

            _state.update { it.copy(
                events = events,
                loading = false
            )}
        }
    }
}