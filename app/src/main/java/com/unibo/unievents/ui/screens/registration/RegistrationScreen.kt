package com.unibo.unievents.ui.screens.registration

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.NavigationRoute
import com.unibo.unievents.ui.screens.login.DialogSection

@Composable
fun RegistrationScreen(
    state: RegistrationState,
    actions: RegistrationActions,
    navController: NavHostController
) {
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTerminiDialog by remember { mutableStateOf(false) }
    var showContattiDialog by remember { mutableStateOf(false) }
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(24.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                        .padding(24.dp)
                ) {
                    Text(
                        text = "UniEvents",
                        color = MaterialTheme.colorScheme.primary,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "La piattaforma per scoprire e partecipare agli eventi universitari dell'Università di Bologna",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        value = state.email,
                        onValueChange = actions.updateEmail,
                        leadingIcon = { Icon(Icons.Filled.Email, "Email") },
                        label = { Text("Email") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = !state.emailError.isNullOrBlank(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.badgeNumber,
                        onValueChange = actions.updateBadgeNumber,
                        leadingIcon = { Icon(Icons.Filled.AccountBox, "Matricola") },
                        label = { Text("Matricola") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = !state.badgeNumberError.isNullOrBlank(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Number
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.password,
                        onValueChange = actions.updatePassword,
                        leadingIcon = { Icon(Icons.Filled.Lock, "Password") },
                        trailingIcon = {
                            IconButton(onClick = actions.toggleShowPassword) {
                                if (state.showPassword) Icon(Icons.Filled.Visibility, "Show password")
                                else Icon(Icons.Filled.VisibilityOff, "Hide password")
                            }
                        },
                        visualTransformation = if (state.showPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        label = { Text("Password") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = !state.passwordError.isNullOrBlank(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = state.passwordConfirm,
                        onValueChange = actions.updatePasswordConfirm,
                        leadingIcon = { Icon(Icons.Filled.Check, "Password") },
                        trailingIcon = {
                            IconButton(onClick = actions.toggleShowConfirmPassword) {
                                if (state.showConfirmPassword) Icon(Icons.Filled.Visibility, "Show password")
                                else Icon(Icons.Filled.VisibilityOff, "Hide password")
                            }
                        },
                        visualTransformation = if (state.showConfirmPassword) VisualTransformation.None
                        else PasswordVisualTransformation(),
                        label = { Text("Password") },
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = !state.passwordConfirmError.isNullOrBlank(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (!state.errorMessage.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = state.errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 4.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = {
                            actions.confirm
                            navController.navigate(NavigationRoute.Splash)
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Registrati")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { }
            ) {
                Text(
                    text = "Esplora eventi senza accedere →",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "© 2026 Università di Bologna - Tutti i diritti riservati",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (showPrivacyDialog) {
                    AlertDialog(
                        onDismissRequest = { showPrivacyDialog = false },
                        title = { Text("Informativa sulla Privacy") },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                DialogSection(
                                    title = "Dati raccolti",
                                    body = "Raccogliamo email, nome e le partecipazione agli eventi per personalizzare la tua esperienza su Uni Events."
                                )
                                DialogSection(
                                    title = "Utilizzo dei dati",
                                    body = "I tuoi dati non vengono mai ceduti a terzi. Sono utilizzati esclusivamente per il funzionamento della piattaforma."
                                )
                                DialogSection(
                                    title = "I tuoi diritti",
                                    body = "Puoi richiedere la cancellazione del tuo account e di tutti i dati associati in qualsiasi momento."
                                )
                            }
                        },
                        confirmButton = {
                            Button(onClick = { showPrivacyDialog = false }) {
                                Text("Chiudi")
                            }
                        }
                    )
                }

                if (showTerminiDialog) {
                    AlertDialog(
                        onDismissRequest = { showTerminiDialog = false },
                        title = { Text("Termini di Utilizzo") },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                DialogSection(
                                    title = "Accesso alla piattaforma",
                                    body = "Uni Events è riservato esclusivamente agli studenti e al personale dell'Università di Bologna."
                                )
                                DialogSection(
                                    title = "Comportamento degli utenti",
                                    body = "È vietato pubblicare contenuti inappropriati, diffondere materiale protetto da copyright o utilizzare la piattaforma per attività commerciali non autorizzate.."
                                )
                                DialogSection(
                                    title = "Modifiche ai termini",
                                    body = "L'università si riserva il diritto di aggiornare i termini con preavviso di 14 giorni via email."
                                )
                            }
                        },
                        confirmButton = {
                            Button(onClick = { showTerminiDialog = false }) {
                                Text("Chiudi")
                            }
                        }
                    )
                }

                if (showContattiDialog) {
                    AlertDialog(
                        onDismissRequest = { showContattiDialog = false },
                        title = { Text("Contattaci") },
                        text = {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                DialogSection(
                                    title = "Supporto tecnico",
                                    body = "Per problemi con l'app scrivi a: matteo.crepaldi4@studio.unibo.it"
                                )
                                DialogSection(
                                    title = "Segnalazioni",
                                    body = "Per segnalare contenuti inappropriati: andrea.monti24@studio.unibo.it"
                                )
                                DialogSection(
                                    title = "Orari di supporto",
                                    body = "Lun–Ven, 9:00–18:00. Risposta entro 1 giorno lavorativo."
                                )
                            }
                        },
                        confirmButton = {
                            Button(onClick = { showContattiDialog = false }) {
                                Text("Chiudi")
                            }
                        }
                    )
                }

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(
                        onClick = { showPrivacyDialog = true },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Privacy",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    TextButton(
                        onClick = { showTerminiDialog = true },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Termini",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    TextButton(
                        onClick = { showContattiDialog = true },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Contatti",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
        }
    }
}
