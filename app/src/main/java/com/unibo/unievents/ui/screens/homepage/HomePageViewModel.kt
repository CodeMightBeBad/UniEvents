package com.unibo.unievents.ui.screens.homepage

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class Event(
    val id: String,
    val title: String,
    val location: String,
    val venue: String,
    val date: LocalDate,
    val time: LocalTime,
    val currentParticipants: Int,
    val maxParticipants: Int,
    val description: String
)

data class EventsByDate(
    val date: LocalDate,
    val events: List<Event>
)

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(
        val eventsGroupedByDate: List<EventsByDate>
    ) : HomeUiState()
    data class Error(val message: String) : HomeUiState()
}

class HomePageViewModel : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadEvents()
    }

    private fun loadEvents() {
        viewModelScope.launch {
            try {
                // Simulazione caricamento dati da repository
                val events = fetchEvents()
                val groupedEvents = groupEventsByDate(events)
                _uiState.value = HomeUiState.Success(groupedEvents)
            } catch (e: Exception) {
                _uiState.value = HomeUiState.Error("Errore nel caricamento degli eventi")
            }
        }
    }

    private fun fetchEvents(): List<Event> {
        // Qui andrebbe la chiamata al repository
        // Per ora restituiamo dati di esempio inclusa la card dell'immagine

        val dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.ITALIAN)

        return listOf(
            Event(
                id = "1",
                title = "Aperitivo di Benvenuto",
                location = "Bologna",
                venue = "Cortile del Campus - Piazza Scaravilli 1",
                date = LocalDate.of(2026, 4, 22),
                time = LocalTime.of(18, 0),
                currentParticipants = 3,
                maxParticipants = 100,
                description = "Aperitivo per conoscere gli studenti del primo anno"
            ),
            // Aggiungi altri eventi di esempio qui
            Event(
                id = "2",
                title = "Workshop di Programmazione",
                location = "Milano",
                venue = "Aula Magna - Via Golgi 42",
                date = LocalDate.of(2026, 4, 23),
                time = LocalTime.of(15, 0),
                currentParticipants = 45,
                maxParticipants = 80,
                description = "Workshop intensivo su Kotlin e Android"
            ),
            Event(
                id = "3",
                title = "Career Day",
                location = "Roma",
                venue = "Centro Congressi - EUR",
                date = LocalDate.of(2026, 4, 25),
                time = LocalTime.of(10, 0),
                currentParticipants = 120,
                maxParticipants = 200,
                description = "Incontra le migliori aziende del settore"
            )
        )
    }

    private fun groupEventsByDate(events: List<Event>): List<EventsByDate> {
        return events
            .groupBy { it.date }
            .map { (date, eventList) ->
                EventsByDate(
                    date = date,
                    events = eventList.sortedBy { it.time }
                )
            }
            .sortedBy { it.date }
    }

    fun formatDate(date: LocalDate): String {
        val formatter = DateTimeFormatter.ofPattern("EEEE d MMMM yyyy", Locale.ITALIAN)
        return date.format(formatter)
    }

    fun formatTime(time: LocalTime): String {
        val formatter = DateTimeFormatter.ofPattern("HH:mm")
        return time.format(formatter)
    }

    fun getParticipantText(current: Int, max: Int): String {
        return "$current / $max partecipanti"
    }

    fun onParticipateClick(event: Event) {
        // Logica per la partecipazione all'evento
        viewModelScope.launch {
            // Implementare la chiamata API per partecipare
        }
    }

    fun onMoreInfoClick(event: Event) {
        // Logica per vedere maggiori informazioni
    }
}