package com.unibo.unievents.ui.screens.creaEvento

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class CreateEventUiState(
    val title: String = "",
    val description: String = "",
    val date: String = "",
    val time: String = "",
    val venueName: String = "",
    val address: String = "",
    val city: String = "",
    val maxParticipants: String = "",
    val images: List<Uri> = emptyList(),
    val isSubmitting: Boolean = false,
    val errorMessage: String? = null,
    val isFormValid: Boolean = false,
    val showImageSourceDialog: Boolean = false
)

sealed class CreateEventResult {
    object Success : CreateEventResult()
    data class Error(val message: String) : CreateEventResult()
}

class CreaEventoViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(CreateEventUiState())
    val uiState: StateFlow<CreateEventUiState> = _uiState.asStateFlow()

    private val _result = MutableStateFlow<CreateEventResult?>(null)
    val result: StateFlow<CreateEventResult?> = _result.asStateFlow()

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title,
            errorMessage = null,
            isFormValid = validateForm(title, _uiState.value.description, _uiState.value.date,
                _uiState.value.time, _uiState.value.venueName, _uiState.value.address,
                _uiState.value.city, _uiState.value.maxParticipants)
        )
    }

    fun updateDescription(description: String) {
        _uiState.value = _uiState.value.copy(
            description = description,
            errorMessage = null,
            isFormValid = validateForm(_uiState.value.title, description, _uiState.value.date,
                _uiState.value.time, _uiState.value.venueName, _uiState.value.address,
                _uiState.value.city, _uiState.value.maxParticipants)
        )
    }

    fun updateDate(date: String) {
        _uiState.value = _uiState.value.copy(
            date = date,
            errorMessage = null,
            isFormValid = validateForm(_uiState.value.title, _uiState.value.description, date,
                _uiState.value.time, _uiState.value.venueName, _uiState.value.address,
                _uiState.value.city, _uiState.value.maxParticipants)
        )
    }

    fun updateTime(time: String) {
        _uiState.value = _uiState.value.copy(
            time = time,
            errorMessage = null,
            isFormValid = validateForm(_uiState.value.title, _uiState.value.description, _uiState.value.date,
                time, _uiState.value.venueName, _uiState.value.address,
                _uiState.value.city, _uiState.value.maxParticipants)
        )
    }

    fun updateVenueName(venueName: String) {
        _uiState.value = _uiState.value.copy(
            venueName = venueName,
            errorMessage = null,
            isFormValid = validateForm(_uiState.value.title, _uiState.value.description, _uiState.value.date,
                _uiState.value.time, venueName, _uiState.value.address,
                _uiState.value.city, _uiState.value.maxParticipants)
        )
    }

    fun updateAddress(address: String) {
        _uiState.value = _uiState.value.copy(
            address = address,
            errorMessage = null,
            isFormValid = validateForm(_uiState.value.title, _uiState.value.description, _uiState.value.date,
                _uiState.value.time, _uiState.value.venueName, address,
                _uiState.value.city, _uiState.value.maxParticipants)
        )
    }

    fun updateCity(city: String) {
        _uiState.value = _uiState.value.copy(
            city = city,
            errorMessage = null,
            isFormValid = validateForm(_uiState.value.title, _uiState.value.description, _uiState.value.date,
                _uiState.value.time, _uiState.value.venueName, _uiState.value.address,
                city, _uiState.value.maxParticipants)
        )
    }

    fun updateMaxParticipants(maxParticipants: String) {
        val filtered = maxParticipants.filter { it.isDigit() }
        val intValue = filtered.toIntOrNull() ?: 0
        val validValue = if (intValue > 1000) "1000" else filtered

        _uiState.value = _uiState.value.copy(
            maxParticipants = validValue,
            errorMessage = null,
            isFormValid = validateForm(_uiState.value.title, _uiState.value.description, _uiState.value.date,
                _uiState.value.time, _uiState.value.venueName, _uiState.value.address,
                _uiState.value.city, validValue)
        )
    }

    fun addImages(uris: List<Uri>) {
        val currentImages = _uiState.value.images.toMutableList()
        currentImages.addAll(uris)
        _uiState.value = _uiState.value.copy(images = currentImages)
    }

    fun addSingleImage(uri: Uri) {
        val currentImages = _uiState.value.images.toMutableList()
        currentImages.add(uri)
        _uiState.value = _uiState.value.copy(images = currentImages)
    }

    fun removeImage(uri: Uri) {
        _uiState.value = _uiState.value.copy(
            images = _uiState.value.images.filter { it != uri }
        )
    }

    fun showImageSourceDialog() {
        _uiState.value = _uiState.value.copy(showImageSourceDialog = true)
    }

    fun hideImageSourceDialog() {
        _uiState.value = _uiState.value.copy(showImageSourceDialog = false)
    }

    private fun validateForm(
        title: String,
        description: String,
        date: String,
        time: String,
        venueName: String,
        address: String,
        city: String,
        maxParticipants: String
    ): Boolean {
        return title.isNotBlank() &&
                description.isNotBlank() &&
                date.isNotBlank() &&
                time.isNotBlank() &&
                venueName.isNotBlank() &&
                address.isNotBlank() &&
                city.isNotBlank() &&
                maxParticipants.isNotBlank() &&
                (maxParticipants.toIntOrNull() ?: 0) > 0
    }

    fun onSubmitClick() {
        val currentState = _uiState.value

        if (!currentState.isFormValid) {
            _uiState.value = currentState.copy(
                errorMessage = "Compila tutti i campi obbligatori (*)"
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, errorMessage = null)

            // Simulazione chiamata API
            val result = submitEvent()

            _uiState.value = _uiState.value.copy(isSubmitting = false)
            _result.value = result
        }
    }

    private suspend fun submitEvent(): CreateEventResult {
        kotlinx.coroutines.delay(1500)
        // Qui andrebbe la chiamata API reale
        return CreateEventResult.Success
    }

    fun onCancelClick() {
        // Naviga indietro
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }

    fun resetForm() {
        _uiState.value = CreateEventUiState()
    }
}