package com.unibo.unievents.ui.screens.createEvent

import android.icu.text.SimpleDateFormat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Title
import androidx.compose.material.icons.outlined.AccessTime
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Event
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDialog
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.navigation.NavHostController
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusProperties
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.unibo.unievents.ui.composables.TopBar
import java.util.Calendar
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateEventScreen(
    state: CreateEventState,
    actions: CreateEventActions,
    navController: NavHostController
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    if (showDatePicker) {
        DatePickerModal(
            onDateSelected = { dateMillis ->
                actions.updateDate(convertMillsToDate(dateMillis))
            },
            onDismiss = {
                showDatePicker = false
            }
        )
    }

    if (showTimePicker) {
        TimePickerModal(
            onTimeSelected = { hours, minutes ->
                actions.updateTime(formatTime(hours, minutes))
            },
            onDismiss = { showTimePicker = false }
        )
    }

    Scaffold(
        topBar = { TopBar(navController, "Crea evento") },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {},
                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer
            ) {
                Icon(Icons.Filled.Check, "Confirm")
            }
        },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            // Warning card
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Il tuo evento verrà revisionato da un amministratore prima di essere pubblicato",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Main information column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = "Informazioni:",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = state.title,
                    onValueChange = actions.updateTitle,
                    label = { Text("Titolo dell'evento") },
                    leadingIcon = { Icon(Icons.Filled.Title, "Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Dropdown menu to fill the address
                ExposedDropdownMenuBox(
                    expanded = state.showAddressSuggestions,
                    onExpandedChange = actions.updateShowSuggestions
                ) {
                    OutlinedTextField(
                        value = state.address,
                        onValueChange = actions.updateAddress,
                        label = { Text("Indirizzo") },
                        leadingIcon = { Icon(Icons.Outlined.LocationOn, "Location") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryEditable)
                    )

                    ExposedDropdownMenu(
                        expanded = !state.addressSuggestions.isEmpty() && state.showAddressSuggestions,
                        onDismissRequest = { actions.updateShowSuggestions(false) }
                    ) {
                        state.addressSuggestions.forEach { address ->
                            DropdownMenuItem(
                                text = { Text(address.name) },
                                onClick = {
                                    actions.updateAddress(address.name)
                                    actions.updateShowSuggestions(false)
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = state.description,
                    onValueChange = actions.updateDescription,
                    label = { Text("Descrizione") },
                    leadingIcon = { Icon(Icons.Outlined.Description, "Description") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = state.date,
                        onValueChange = actions.updateDate,
                        label = { Text("Data") },
                        leadingIcon = {
                            IconButton(onClick = { showDatePicker = true }) {
                                Icon(Icons.Outlined.Event, "Event date")
                            }
                        },
                        readOnly = true,
                        modifier = Modifier.weight(1f)
                    )

                    OutlinedTextField(
                        value = state.time,
                        onValueChange = actions.updateTime,
                        label = { Text("Ora") },
                        leadingIcon = {
                            IconButton(onClick = { showTimePicker = true }) {
                                Icon(Icons.Outlined.AccessTime, "Event time")
                            }
                        },
                        readOnly = true,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Photos column
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = "Immagini:",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    Text(
                        text = "Non sono ancora state aggiunte immagini",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerModal(
    onDateSelected: (Long) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    DatePickerDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = {
                    onDateSelected(datePickerState.selectedDateMillis!!)
                    onDismiss()
                },
                enabled = datePickerState.selectedDateMillis != null
            ) {
                Text("Ok")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerModal(
    onTimeSelected: (Int, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val timePickerState = rememberTimePickerState(is24Hour = true)

    TimePickerDialog(
        title = { Text("Scegli l'ora") },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton( onClick = {
                    onTimeSelected(timePickerState.hour, timePickerState.minute)
                    onDismiss()
                }
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("ANNULLA")
            }
        }
    ) {
        TimePicker(timePickerState)
    }
}

private fun convertMillsToDate(millis: Long): String {
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(Date(millis))
}

private fun formatTime(hours: Int, minutes: Int): String {
    val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
    val calendar = Calendar.getInstance()

    calendar.clear()
    calendar.set(Calendar.HOUR_OF_DAY, hours)
    calendar.set(Calendar.MINUTE, minutes)

    return formatter.format(calendar.time)
}
