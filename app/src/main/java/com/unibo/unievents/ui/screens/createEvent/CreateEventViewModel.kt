package com.unibo.unievents.ui.screens.createEvent

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.EventInsert
import com.unibo.unievents.data.repositories.EventRepository
import com.unibo.unievents.data.repositories.MapRepository
import com.unibo.unievents.data.repositories.MapResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

data class CreateEventState(
    val title: String = "",
    val address: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val maxPeople: Int = 0,
    val addressSuggestions: List<MapResult> = emptyList(),
    val photos: List<Uri> = emptyList(),
    val showAddressSuggestions: Boolean = false
)

data class CreateEventActions(
    val updateTitle: (String) -> Unit,
    val updateAddress: (String) -> Unit,
    val updateDescription: (String) -> Unit,
    val updateDate: (String) -> Unit,
    val updateTime: (String) -> Unit,
    val updateMaxPeople: (Int) -> Unit,
    val updateShowSuggestions: (Boolean) -> Unit,
    val addPhoto: (Uri) -> Unit,
    val confirmCreate: (List<ByteArray>) -> Unit
)

class CreateEventViewModel(
    private val eventRepo: EventRepository,
    private val mapRepo: MapRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CreateEventState())
    val state = _state.asStateFlow()
    
    val actions = CreateEventActions(
        updateTitle = { newTitle ->
            _state.update { it.copy(title = newTitle) }
        },
        updateAddress = { newAddress ->
            _state.update { it.copy(address = newAddress) }

            if (newAddress.length > 5 && state.value.showAddressSuggestions) {
                fetchSuggestions()
            }
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
                val eventInsert = EventInsert(
                    title = state.value.title,
                    address = state.value.address,
                    description = state.value.description,
                    date = LocalDate.parse(state.value.date, dateFormat),
                    time = LocalTime.parse(state.value.time, timeFormat),
                    maxParticipants = if (state.value.maxPeople == 0) null else state.value.maxPeople
                )
                val result = eventRepo.createEvent(eventInsert, photos)
                if (result.isFailure) {
                    android.util.Log.e("CreateEvent", "Error: ${result.exceptionOrNull()?.message}")
                } else {
                    android.util.Log.d("CreateEvent", "Success")
                }
            }
        }
    )

    private fun fetchSuggestions() {
        viewModelScope.launch {
            val results = mapRepo.addressLookup(state.value.address)
            _state.update { it.copy(addressSuggestions = results) }
        }
    }
}