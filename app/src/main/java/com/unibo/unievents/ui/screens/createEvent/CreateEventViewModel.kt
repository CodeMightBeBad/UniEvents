package com.unibo.unievents.ui.screens.createEvent

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.EventInsert
import com.unibo.unievents.data.repositories.EventRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.char

data class CreateEventState(
    val title: String = "title",
    val description: String = "desc",
    val date: String = "12/12/2025",
    val time: String = "15:00",
    val address: String = "via Roma 4",
    val maxParticipants: Int? = 50,

    val isLoading: Boolean = false,
    val isFormValid: Boolean = false
)

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
            _state.update { it.copy(time = time) }
        },
        updateAddress = { address ->
            _state.update { it.copy(address = address) }
        },
        updateMaxParticipants = { maxParticipants ->
            _state.update { it.copy(maxParticipants = maxParticipants.toInt()) }
        },
        submit = {
            val dateFormat = LocalDate.Format {
                dayOfMonth()
                char('/')
                monthNumber()
                char('/')
                year()
            }

            val event = EventInsert(
                title = state.value.title,
                description = state.value.description,
                date = LocalDate.parse(state.value.date, dateFormat),
                time = LocalTime.parse(state.value.time),
                address = state.value.address,
                maxParticipants = state.value.maxParticipants
            )

            try {
                viewModelScope.launch {
                    val result = repository.createEvent(event)
                    result.onFailure { Log.d("CreateEvent", "Error during request") }
                }
            } catch (ex: Exception) {
                Log.d("CreateEvent", ex.message.toString())
            }
        }
    )
}
