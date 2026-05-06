package com.unibo.unievents.ui.screens.createEvent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.EventInsert
import com.unibo.unievents.data.repositories.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime

data class CreateEventState(
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val address: String = "",
    val maxParticipants: Int? = 0,
    val isLoading: Boolean = false
) {
    val isFormValid: Boolean
        get() = title.isNotBlank() &&
                date.length == 10 &&
                time.length == 5 &&
                address.isNotBlank() &&
                (maxParticipants ?: 0) > 0
}

data class CreateEventActions(
    val updateTitle: (String) -> Unit,
    val updateDescription: (String) -> Unit,
    val updateDate: (String) -> Unit,
    val updateTime: (String) -> Unit,
    val updateAddress: (String) -> Unit,
    val updateMaxParticipants: (String) -> Unit,
    val submit: () -> Unit
)

class CreateEventViewModel(private val repository: EventRepository) : ViewModel() {
    private val _state = MutableStateFlow(CreateEventState())
    val state = _state.asStateFlow()

    val actions = CreateEventActions(
        updateTitle = { title ->
            _state.update { it.copy(title = title) }
        },
        updateDescription = { description ->
            _state.update { it.copy(description = description) }
        },
        updateDate = { date ->
            _state.update { it.copy(date = date) }
        },
        updateTime = { time ->
            if (time.length <= 5) {
                _state.update { it.copy(time = time) }
            }
        },
        updateAddress = { address ->
            _state.update { it.copy(address = address) }
        },
        updateMaxParticipants = { maxParticipants ->
            val filtered = maxParticipants.filter { it.isDigit() }
            val value = if (filtered.isEmpty()) 0 else filtered.toIntOrNull() ?: 0
            _state.update { it.copy(maxParticipants = value) }
        },
        submit = {
            val currentState = state.value

            if (currentState.date.length != 10) {
                Log.d("CreateEvent", "Data non completa: ${currentState.date}")
                return@CreateEventActions

            }

            if (currentState.time.length != 5) {
                Log.d("CreateEvent", "Ora non completa: ${currentState.time}")
                return@CreateEventActions

            }

            if (!currentState.date.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
                Log.d("CreateEvent", "Formato data non valido")
                return@CreateEventActions

            }

            if (!currentState.time.matches(Regex("\\d{2}:\\d{2}"))) {
                Log.d("CreateEvent", "Formato ora non valido")
                return@CreateEventActions
            }

            try {
                val dateParts = currentState.date.split("/")
                val timeParts = currentState.time.split(":")

                val day = dateParts[0].toInt()
                val month = dateParts[1].toInt()
                val year = dateParts[2].toInt()
                val hour = timeParts[0].toInt()
                val minute = timeParts[1].toInt()

                val event = EventInsert(
                    title = currentState.title,
                    description = currentState.description,
                    date = LocalDate(year, month, day),
                    time = LocalTime(hour, minute),
                    address = currentState.address,
                    maxParticipants = currentState.maxParticipants ?: 0
                )

                viewModelScope.launch {
                    try {
                        val result = repository.createEvent(event)
                        result.onFailure { Log.d("CreateEvent", "Error: ${it.message}") }
                    } catch (ex: Exception) {
                        Log.d("CreateEvent", ex.message.toString())
                    }
                }
            } catch (ex: Exception) {
                Log.d("CreateEvent", "Errore parsing: ${ex.message}")
            }
        }
    )
}