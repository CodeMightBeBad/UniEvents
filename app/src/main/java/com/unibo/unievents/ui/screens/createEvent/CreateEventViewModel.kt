package com.unibo.unievents.ui.screens.createEvent

import android.net.Uri
import android.util.Log
import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.EventInsert
import com.unibo.unievents.data.repositories.EventRepository
import com.unibo.unievents.data.repositories.MapRepository
import com.unibo.unievents.data.repositories.MapResult
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char
import kotlin.time.Duration.Companion.milliseconds

data class CreateEventState(
    val title: String = "",
    val address: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val maxPeople: String = "",
    val addressSuggestions: List<MapResult> = emptyList(),
    val photos: List<Uri> = emptyList(),

    val showAddressSuggestions: Boolean = false,
    val textFieldsError: Boolean = false,
    val searchingAddress: Boolean = false,
    val selectedAddress: Boolean = false
)

data class CreateEventActions(
    val updateTitle: (String) -> Unit,
    val updateAddress: (String) -> Unit,
    val updateDescription: (String) -> Unit,
    val updateDate: (String) -> Unit,
    val updateTime: (String) -> Unit,
    val updateMaxPeople: (String) -> Unit,
    val updateShowSuggestions: (Boolean) -> Unit,
    val addPhoto: (Uri) -> Unit,
    val confirmCreate: (List<ByteArray>) -> Unit,
    val checkFields: () -> Boolean,
    val selectAddress: (String) -> Unit
)

@OptIn(FlowPreview::class)
class CreateEventViewModel(
    private val eventRepo: EventRepository,
    private val mapRepo: MapRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CreateEventState())
    val state = _state.asStateFlow()

    // Only search the address after 1 second the user hasn't changed it to avoid hitting rate limits
    init {
        viewModelScope.launch {
            _state
                .map { it.address }
                .distinctUntilChanged()
                .debounce(1000L.milliseconds)
                .filter { it.length >= 5 }
                .collect { address ->
                    fetchSuggestions(address)
                }
        }
    }

    val actions = CreateEventActions(
        updateTitle = { newTitle ->
            _state.update { it.copy(title = newTitle) }
        },
        updateAddress = { newAddress ->
            _state.update { it.copy(
                address = newAddress,
                selectedAddress = false
            )}
        },
        updateDescription = { newDescription ->
            _state.update { it.copy(description = newDescription) }
        },
        updateDate = { newDate ->
            _state.update { it.copy(date = newDate) }
        },
        updateTime = { newTime ->
            _state.update { it.copy(time = newTime) }
        },
        updateMaxPeople = { newMaxPeople ->
            _state.update { it.copy(maxPeople = newMaxPeople) }
        },
        updateShowSuggestions = { show ->
            _state.update { it.copy(showAddressSuggestions = show) }
        },
        addPhoto = { uri ->
            _state.update { it.copy(photos = it.photos + uri) }
        },
        confirmCreate = { photos ->
            viewModelScope.launch {
                val dateFormat = LocalDate.Format {
                    dayOfMonth(); char('/'); monthNumber(); char('/'); year()
                }

                val timeFormat = LocalTime.Format {
                    hour(); char(':'); minute()
                }

                val address = state.value.addressSuggestions.first { it.formatAddress() == state.value.address }

                val eventInsert = EventInsert(
                    title = state.value.title,
                    address = state.value.address,
                    description = state.value.description,
                    date = LocalDate.parse(state.value.date, dateFormat),
                    time = LocalTime.parse(state.value.time, timeFormat),
                    maxParticipants = state.value.maxPeople.toIntOrNull(),
                    latitude = address.latitude,
                    longitude = address.longitude
                )

                val result = eventRepo.createEvent(eventInsert, photos)

                if (result.isFailure) {
                    Log.e("CreateEvent", "Error: ${result.exceptionOrNull()?.message}")
                } else {
                    Log.d("CreateEvent", "Success")
                }
            }
        },
        checkFields = { checkTextFields() },
        selectAddress = { address ->
            _state.update { it.copy(
                selectedAddress = true,
                address = address,
                showAddressSuggestions = false
            )}
        }
    )

    private fun fetchSuggestions(address: String) {
        if (!state.value.selectedAddress) {
            viewModelScope.launch {
                _state.update { it.copy(searchingAddress = true) }

                try {
                    val results = mapRepo.addressLookup(address)
                    _state.update {
                        it.copy(
                            addressSuggestions = results,
                            showAddressSuggestions = true
                        )
                    }
                } catch (_: Exception) {
                    Log.d("CreateEventViewModel", "Exception on address lookup")
                } finally {
                    _state.update { it.copy(searchingAddress = false) }
                }
            }
        }
    }

    private fun checkTextFields(): Boolean {
        // Check that no field is left empty
        if (state.value.title.isBlank() ||
                state.value.address.isBlank() ||
                state.value.description.isBlank() ||
                state.value.date.isBlank() ||
                state.value.time.isBlank()
        ) return false

        // Check that (if the field is populated) maxPeople only contains numbers
        if (!state.value.maxPeople.isEmpty() && !state.value.maxPeople.isDigitsOnly()) return false

        if (!state.value.selectedAddress) return false

        return true
    }
}