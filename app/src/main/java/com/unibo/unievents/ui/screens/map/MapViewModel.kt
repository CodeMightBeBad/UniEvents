package com.unibo.unievents.ui.screens.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class EventLocation(
    val id: String,
    val title: String,
    val location: String,
    val address: String,
    val latitude: Double,
    val longitude: Double,
    val date: String,
    val time: String,
    val participants: Int,
    val maxParticipants: Int,
    val description: String = ""
)

data class MapUiState(
    val events: List<EventLocation> = emptyList(),
    val selectedEvent: EventLocation? = null,
    val isLocationPermissionGranted: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MapViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

            // Dati di esempio per i luoghi degli eventi
            val events = listOf(
                EventLocation(
                    id = "1",
                    title = "Hackathon UniBO 2026",
                    location = "Laboratorio Informatica",
                    address = "Via Mura Anteo Zamboni 7, Bologna",
                    latitude = 44.4949,
                    longitude = 11.3530,
                    date = "10 mar 2028",
                    time = "09:00",
                    participants = 45,
                    maxParticipants = 100,
                    description = "Hackathon dedicato all'innovazione tecnologica"
                ),
                EventLocation(
                    id = "2",
                    title = "Aperitivo di Benvenuto",
                    location = "Sala Feste Campus",
                    address = "Piazza Scaravilli 1, Bologna",
                    latitude = 44.4965,
                    longitude = 11.3518,
                    date = "22 apr 2026",
                    time = "18:00",
                    participants = 3,
                    maxParticipants = 100,
                    description = "Aperitivo per conoscere gli studenti del primo anno"
                ),
                EventLocation(
                    id = "3",
                    title = "Career Day 2026",
                    location = "Centro Congressi",
                    address = "Piazza della Costituzione 4, Bologna",
                    latitude = 44.5038,
                    longitude = 11.3406,
                    date = "15 mag 2026",
                    time = "10:00",
                    participants = 120,
                    maxParticipants = 200,
                    description = "Incontra le migliori aziende del settore"
                )
            )

            _uiState.value = _uiState.value.copy(
                events = events,
                isLoading = false
            )
        }
    }

    fun selectEvent(event: EventLocation) {
        _uiState.value = _uiState.value.copy(selectedEvent = event)
    }

    fun clearSelectedEvent() {
        _uiState.value = _uiState.value.copy(selectedEvent = null)
    }

    fun updatePermissionStatus(granted: Boolean) {
        _uiState.value = _uiState.value.copy(isLocationPermissionGranted = granted)
    }

    fun formatParticipantText(current: Int, max: Int): String = "$current / $max partecipanti"
}