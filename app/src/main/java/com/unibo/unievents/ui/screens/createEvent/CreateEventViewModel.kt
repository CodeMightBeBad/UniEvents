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
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

data class CreateEventState(
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val address: String = "",
    val maxParticipants: Int? = 0,
    val isLoading: Boolean = false,
    val dateError: String? = null
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
    val submit: () -> Unit,
    val setDateError: (String?) -> Unit
)

class CreateEventViewModel(private val repository: EventRepository) : ViewModel() {
    private val _state = MutableStateFlow(CreateEventState())
    val state = _state.asStateFlow()

    private fun isValidDate(date: String, time: String): Boolean {
        try {
            if (date.length != 8 || time.length != 4) return false

            val day = date.substring(0, 2).toInt()
            val month = date.substring(2, 4).toInt()
            val year = date.substring(4, 8).toInt()

            val hour = time.substring(0, 2).toInt()
            val minute = time.substring(2, 4).toInt()

            if (day < 1 || day > 31) {
                Log.d("CreateEvent", "Giorno non valido: $day")
                return false
            }
            if (month < 1 || month > 12) {
                Log.d("CreateEvent", "Mese non valido: $month")
                return false
            }
            if (year < 2026 || year > 2100) {
                Log.d("CreateEvent", "Anno non valido: $year")
                return false
            }
            if (hour < 0 || hour > 23) {
                Log.d("CreateEvent", "Ora non valida: $hour")
                return false
            }
            if (minute < 0 || minute > 59) {
                Log.d("CreateEvent", "Minuto non valido: $minute")
                return false
            }

            val localDate = try {
                LocalDate(year, month, day)
            } catch (e: Exception) {
                Log.d("CreateEvent", "Data inesistente: $day/$month/$year")
                return false
            }

            val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
            if (localDate < today) {
                Log.d("CreateEvent", "Data passata")
                return false
            }

            return true
        } catch (e: Exception) {
            Log.d("CreateEvent", "Errore: ${e.message}")
            return false
        }
    }

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
        setDateError = { error ->
            _state.update { it.copy(dateError = error) }
        },
        submit = {
            val currentState = state.value

            _state.update { it.copy(dateError = null) }

            if (currentState.date.length == 8 && currentState.time.length == 4) {
                if (isValidDate(currentState.date, currentState.time)) {
                    // DATA VALIDA - CREA EVENTO
                    try {
                        val day = currentState.date.substring(0, 2).toInt()
                        val month = currentState.date.substring(2, 4).toInt()
                        val year = currentState.date.substring(4, 8).toInt()
                        val hour = currentState.time.substring(0, 2).toInt()
                        val minute = currentState.time.substring(2, 4).toInt()

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
                                result.onSuccess { Log.d("CreateEvent", "Evento creato!") }
                            } catch (ex: Exception) {
                                Log.d("CreateEvent", ex.message.toString())
                            }
                        }
                    } catch (ex: Exception) {
                        Log.d("CreateEvent", "Errore: ${ex.message}")
                    }
                } else {
                    _state.update { it.copy(dateError = "Data o ora non valida") }
                }
            } else {
                _state.update { it.copy(dateError = "Data o ora incompleta") }
            }
        }
    )
}