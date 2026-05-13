package com.unibo.unievents.ui.screens.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.repositories.EventRepository
import com.unibo.unievents.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

data class HomePageState(
    val events: List<Event> = emptyList(),
    val joinedEvents: List<Event> = emptyList(),
    val loading: Boolean = false,
    val selectedDate: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault()),
    val today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())
)

data class HomePageActions(
    val joinEvent: (Int) -> Unit,
    val leaveEvent: (Int) -> Unit,
    val isEventJoined: (Int) -> Boolean,
    val goToPreviousDay: () -> Unit,
    val goToNextDay: () -> Unit
)

class HomePageViewModel(
    private val eventsRepo: EventRepository,
    private val userRepo: UserRepository
) : ViewModel() {

    private val _state = MutableStateFlow(HomePageState())
    val state = _state.asStateFlow()

    val actions = HomePageActions(
        joinEvent = { eventID ->
            viewModelScope.launch {
                userRepo.joinEvent(eventID)
                fetchEvents()
            }
        },
        leaveEvent = { eventID ->
            viewModelScope.launch {
                userRepo.leaveEvent(eventID)
                fetchEvents()
            }
        },
        isEventJoined = { eventID ->
            state.value.joinedEvents.any { event -> event.id == eventID }
        },
        goToPreviousDay = {
            _state.update { current ->
                val prev = current.selectedDate.minus(1, DateTimeUnit.DAY)
                if (prev < current.today) current
                else current.copy(selectedDate = prev)
            }
            fetchEvents()
        },
        goToNextDay = {
            _state.update { current ->
                current.copy(selectedDate = current.selectedDate.plus(1, DateTimeUnit.DAY))
            }
            fetchEvents()
        }
    )

    fun fetchEvents() {
        viewModelScope.launch {
            val selectedDate = _state.value.selectedDate

            val allApprovedEvents = eventsRepo.getApprovedEvents()

            val eventList = allApprovedEvents.filter { event ->

                val isSameDate = event.date.year == selectedDate.year &&
                        event.date.monthNumber == selectedDate.monthNumber &&
                        event.date.dayOfMonth == selectedDate.dayOfMonth

                isSameDate
            }

            val joinedEventsList = userRepo.getJoinedEvents()

            _state.update {
                it.copy(
                    events = eventList,
                    joinedEvents = joinedEventsList,
                    loading = false
                )
            }
        }
    }

    init {
        fetchEvents()
    }
}