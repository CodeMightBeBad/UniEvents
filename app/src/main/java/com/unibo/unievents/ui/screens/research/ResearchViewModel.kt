package com.unibo.unievents.ui.screens.research

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.repositories.EventRepository
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.todayIn

data class ResearchState(
    val searchQuery: String = "",
    val allEvents: List<Event> = emptyList(),
    val filteredEvents: List<Event> = emptyList(),
    val loading: Boolean = false,
    val eventsCount: Int = 0
)

data class ResearchActions(
    val onSearchQueryChange: (String) -> Unit,
    val onEventClick: (Event) -> Unit
)

fun formatEventDate(date: LocalDate): String {
    return "${date.dayOfMonth.toString().padStart(2, '0')}-${date.monthNumber.toString().padStart(2, '0')}-${date.year}"
}

class ResearchViewModel(
    private val repository: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow(ResearchState())
    val state = _state.asStateFlow()

    private val searchQueryFlow = MutableStateFlow("")

    val actions = ResearchActions(
        onSearchQueryChange = { query ->
            _state.update { it.copy(searchQuery = query) }
            searchQueryFlow.value = query
        },
        onEventClick = { }
    )

    init {
        fetchApprovedEvents()
        setupSearchDebounce()
    }

    fun fetchApprovedEvents() {
        viewModelScope.launch {
            _state.update { it.copy(loading = true) }
            try {
                val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
                val events = repository.getApprovedEvents()
                    .filter { event -> event.date >= today }

                _state.update {
                    it.copy(
                        allEvents = events,
                        filteredEvents = events,
                        eventsCount = events.size,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false) }
            }
        }
    }

    @OptIn(FlowPreview::class)
    private fun setupSearchDebounce() {
        viewModelScope.launch {
            searchQueryFlow
                .debounce(300)
                .distinctUntilChanged()
                .collect { query ->
                    performSearch(query)
                }
        }
    }

    private fun performSearch(query: String) {
        viewModelScope.launch {
            _state.update { currentState ->
                if (query.isBlank()) {
                    currentState.copy(
                        filteredEvents = currentState.allEvents,
                        eventsCount = currentState.allEvents.size
                    )
                } else {
                    val filtered = currentState.allEvents.filter { event ->
                        matchesSearchQuery(event, query)
                    }

                    currentState.copy(
                        filteredEvents = filtered,
                        eventsCount = filtered.size
                    )
                }
            }
        }
    }

    private fun matchesSearchQuery(event: Event, query: String): Boolean {
        val lowercaseQuery = query.lowercase()
        val matchesTitle = event.title.lowercase().contains(lowercaseQuery)
        val matchesAddress = event.address.lowercase().contains(lowercaseQuery)
        val matchesDate = formatEventDate(event.date).contains(lowercaseQuery) // già LocalDate
        return matchesTitle || matchesAddress || matchesDate
    }

}

