package com.unibo.unievents.ui.screens.map

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Coordinates
import com.unibo.unievents.data.LocationService
import com.unibo.unievents.data.repositories.EventRepository
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
    private val context: Context,
    private val eventRepository: EventRepository
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
            try {
                val rawEvents = eventRepository.getApprovedEvents()
                android.util.Log.d("MapDebug", "Eventi dal DB: ${rawEvents.size}")

                val events = rawEvents.mapNotNull { event ->
                    android.util.Log.d("MapDebug", "Geocoding: ${event.address}")
                    val coords = eventRepository.getCoordinatesFromAddress(event.address)
                    android.util.Log.d("MapDebug", "Coords per ${event.address}: $coords")
                    if (coords == null) return@mapNotNull null
                    val participantsCount = eventRepository.getPeopleCount(event)
                    EventLocation(
                        id = event.id.toString(),
                        title = event.title,
                        location = event.address,
                        address = event.address,
                        latitude = coords.first,
                        longitude = coords.second,
                        date = event.date.toString(),
                        time = event.time.toString(),
                        participants = participantsCount,
                        maxParticipants = event.maxParticipants ?: 0,
                        description = event.description
                    )
                }
                android.util.Log.d("MapDebug", "EventLocation finali: ${events.size}")
                _uiState.value = _uiState.value.copy(events = events, isLoading = false)
            } catch (e: Exception) {
                android.util.Log.e("MapDebug", "Errore: ${e.message}", e)
                _uiState.value = _uiState.value.copy(error = e.message, isLoading = false)
            }
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