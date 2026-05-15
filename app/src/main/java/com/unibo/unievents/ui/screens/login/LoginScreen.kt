package com.unibo.unievents.ui.screens.login

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
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.material3.OutlinedButton
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
import androidx.navigation.NavHostController
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import com.unibo.unievents.ui.NavigationRoute

@Composable
fun LoginScreen(
    state: LoginState,
    actions: LoginActions,
    navController: NavHostController
) {
    var showPrivacyDialog by remember { mutableStateOf(false) }
    var showTerminiDialog by remember { mutableStateOf(false) }
    var showContattiDialog by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Uni Events",
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
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
                        leadingIcon = {
                            Icon(Icons.Filled.Email, "matricola")
                        },
                        value = state.email,
                        onValueChange = actions.updateEmail,
                        label = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = state.errorMessage != null
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, "password")
                        },
                        value = state.password,
                        onValueChange = actions.updatePassword,
                        label = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        isError = state.errorMessage != null,
                        visualTransformation = if (state.isPasswordVisible)
                            VisualTransformation.None
                        else
                            PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = actions.togglePasswordVisibility) {
                                Icon(
                                    imageVector = if (state.isPasswordVisible)
                                        Icons.Filled.VisibilityOff
                                    else
                                        Icons.Filled.Visibility,
                                    contentDescription = if (state.isPasswordVisible)
                                        "Nascondi password"
                                    else
                                        "Mostra password"
                                )
                            }
                        }
                    )

                    if (state.errorMessage != null) {
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
                            actions.confirm()
                            navController.navigate(NavigationRoute.Splash)
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Accedi")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "oppure",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedButton(
                        onClick = { navController.navigate(NavigationRoute.Register) },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Registrati ora")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    TextButton(
                        onClick = { }
                    ) {
                        Text(
                            text = "Password dimenticata?",
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextButton(
                onClick = { }
            ) {
                Text(
                    text = "Esplora eventi senza accedere →",
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

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
