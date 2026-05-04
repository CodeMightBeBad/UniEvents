package com.unibo.unievents.ui.screens.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.repositories.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomePageState (
    val events: List<Event> = emptyList(),
    val loading: Boolean = false
)

class HomePageViewModel(private val repository: EventRepository) : ViewModel() {
    private val _state = MutableStateFlow(HomePageState())
    val state = _state.asStateFlow()

    fun fetchEvents() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }

            val eventList = repository.getApprovedEvents()
            _state.update { it.copy(events = eventList) }

            _state.update { it.copy(loading = false) }
        }
    }

    init {
        fetchEvents()
    }
}
