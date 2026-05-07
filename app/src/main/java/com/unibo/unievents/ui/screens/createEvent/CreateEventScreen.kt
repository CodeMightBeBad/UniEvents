package com.unibo.unievents.ui.screens.createEvent

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Abc
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Title
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.TopBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import kotlinx.coroutines.launch

@Composable
fun CreateEventScreen(
    state: CreateEventState,
    actions: CreateEventActions,
    navController: NavHostController
) {
    var addressSearchDebounce by remember { mutableStateOf<kotlinx.coroutines.Job?>(null) }
    val scope = rememberCoroutineScope()
    val focusRequester = remember { FocusRequester() }
    var addressTextFieldValue by remember { mutableStateOf(TextFieldValue(state.address)) }

    LaunchedEffect(state.address) {
        if (addressTextFieldValue.text != state.address) {
            addressTextFieldValue = TextFieldValue(state.address, selection = TextRange(state.address.length))
        }
    }

    Scaffold(
        topBar = {
            TopBar(navController, "Crea un Evento")
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Text(
                    text = "Il tuo evento verrà revisionato da un amministratore prima di essere pubblicato",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(16.dp)
                )
            }

            Text(
                text = "Immagini Evento",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            OutlinedButton(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    Icons.Default.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text("AGGIUNGI FOTO")
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.Title, "titolo")
                },
                label = { Text("Inserisci il titolo*") },
                value = state.title,
                onValueChange = actions.updateTitle,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            OutlinedTextField(
                value = addressTextFieldValue,
                onValueChange = { newValue ->
                    addressTextFieldValue = newValue
                    actions.updateAddress(newValue.text)

                    addressSearchDebounce?.cancel()
                    addressSearchDebounce = scope.launch {
                        kotlinx.coroutines.delay(300)
                        actions.searchAddress(newValue.text)
                    }
                },
                leadingIcon = {
                    Icon(Icons.Filled.Place, "indirizzo")
                },
                label = { Text("Indirizzo*") },
                modifier = Modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester),
                shape = RoundedCornerShape(12.dp),
                trailingIcon = {
                    if (state.isAddressLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                    }
                }
            )

            if (state.addressSuggestions.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        state.addressSuggestions.forEach { suggestion ->
                            TextButton(
                                onClick = {
                                    addressTextFieldValue = TextFieldValue(suggestion.displayName, selection = TextRange(suggestion.displayName.length))
                                    actions.selectAddress(suggestion.displayName)
                                    focusRequester.requestFocus()
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = suggestion.displayName,
                                    modifier = Modifier.padding(12.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (suggestion != state.addressSuggestions.last()) {
                                HorizontalDivider()
                            }
                        }
                    }
                }
            }

            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.Abc, "descrizione")
                },
                label = { Text("Descrivi il tuo evento") },
                value = state.description,
                onValueChange = actions.updateDescription,
                modifier = Modifier.fillMaxWidth(),
                minLines = 4,
                maxLines = 6,
                shape = RoundedCornerShape(12.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        leadingIcon = { Icon(Icons.Filled.DateRange, "data") },
                        label = { Text("Data*") },
                        value = state.date,
                        onValueChange = { newValue ->
                            val digits = newValue.filter { it.isDigit() }
                            if (digits.length <= 8) {
                                actions.updateDate(digits)
                                actions.setDateError(null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("gg/mm/aaaa") },
                        isError = state.dateError != null,
                        supportingText = {
                            if (state.dateError != null) {
                                Text(
                                    text = state.dateError,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        visualTransformation = { text ->
                            val digits = text.text.filter { it.isDigit() }
                            val formatted = when (digits.length) {
                                0 -> ""
                                1, 2 -> digits
                                3, 4 -> "${digits.take(2)}/${digits.drop(2)}"
                                else -> "${digits.take(2)}/${digits.substring(2, 4)}/${digits.drop(4)}"
                            }
                            TransformedText(
                                text = androidx.compose.ui.text.AnnotatedString(formatted),
                                offsetMapping = object : OffsetMapping {
                                    override fun originalToTransformed(offset: Int): Int {
                                        if (offset == 0) return 0
                                        return when (offset) {
                                            1, 2 -> offset
                                            3, 4 -> offset + 1
                                            else -> offset + 2
                                        }.coerceIn(0, formatted.length)
                                    }
                                    override fun transformedToOriginal(offset: Int): Int {
                                        if (offset == 0) return 0
                                        return when (offset) {
                                            1, 2 -> offset
                                            3, 4 -> offset - 1
                                            else -> offset - 2
                                        }.coerceIn(0, text.text.length)
                                    }
                                }
                            )
                        }
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        leadingIcon = { Icon(Icons.Filled.AccessTime, "ora") },
                        label = { Text("Ora*") },
                        value = state.time,
                        onValueChange = { newValue ->
                            val digits = newValue.filter { it.isDigit() }
                            if (digits.length <= 4) {
                                actions.updateTime(digits)
                                actions.setDateError(null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        placeholder = { Text("hh:mm") },
                        isError = state.dateError != null,
                        visualTransformation = { text ->
                            val digits = text.text.filter { it.isDigit() }
                            val formatted = when (digits.length) {
                                0 -> ""
                                1, 2 -> digits
                                else -> "${digits.take(2)}:${digits.drop(2)}"
                            }
                            TransformedText(
                                text = androidx.compose.ui.text.AnnotatedString(formatted),
                                offsetMapping = object : OffsetMapping {
                                    override fun originalToTransformed(offset: Int): Int {
                                        if (offset == 0) return 0
                                        return if (offset <= 2) offset else offset + 1
                                    }
                                    override fun transformedToOriginal(offset: Int): Int {
                                        if (offset == 0) return 0
                                        return if (offset <= 2) offset else offset - 1
                                    }
                                }
                            )
                        }
                    )
                }
            }

            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.Person, "persone")
                },
                label = { Text("Numero massimo partecipanti*") },
                value = state.maxParticipants.toString(),
                onValueChange = actions.updateMaxParticipants,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = actions.submit,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    enabled = state.title.isNotBlank() &&
                            state.date.length == 8 &&
                            state.time.length == 4 &&
                            state.address.isNotBlank() &&
                            (state.maxParticipants ?: 0) > 0
                ) {
                    Text("RICHIEDI PUBBLICAZIONE")
                }

                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ANNULLA")
                    (state.maxParticipants ?: 0) > 0
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
