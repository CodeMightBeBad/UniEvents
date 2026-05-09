package com.unibo.unievents.ui.screens.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Coordinates
import com.unibo.unievents.data.LocationService
import com.unibo.unievents.utils.PermissionStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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
    val permissionStatus: Map<String, PermissionStatus> = emptyMap(),
    val isLocationEnabled: Boolean = true,
    val currentLocation: Coordinates? = null,
    val isLoadingLocation: Boolean = false,
    val isLoading: Boolean = false,
    val error: String? = null
)

class MapViewModel(
    private val context: Context
) : ViewModel() {

    private val locationService = LocationService(context)

    private val _uiState = MutableStateFlow(MapUiState())
    val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
        observeLocation()
    }

    private fun observeLocation() {
        locationService.coordinates
            .onEach { coordinates ->
                _uiState.value = _uiState.value.copy(
                    currentLocation = coordinates,
                    isLoadingLocation = false
                )
            }
            .launchIn(viewModelScope)

        viewModelScope.launch {
            locationService.isLoading
                .collect { isLoading ->
                    _uiState.value = _uiState.value.copy(isLoadingLocation = isLoading)
                }
        }
    }

    private fun loadEvents() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)

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

    fun updatePermissionStatus(statuses: Map<String, PermissionStatus>) {
        _uiState.value = _uiState.value.copy(permissionStatus = statuses)
    }

    fun updateLocationEnabled(enabled: Boolean) {
        _uiState.value = _uiState.value.copy(isLocationEnabled = enabled)
    }

    suspend fun getCurrentLocation(): Coordinates? {
        return try {
            locationService.getCurrentLocation()
        } catch (e: IllegalStateException) {
            _uiState.value = _uiState.value.copy(isLocationEnabled = false)
            null
        } catch (e: SecurityException) {
            null
        }
    }

    fun formatParticipantText(current: Int, max: Int): String = "$current / $max partecipanti"
}