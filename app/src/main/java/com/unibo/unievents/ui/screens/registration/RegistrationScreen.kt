package com.unibo.unievents.ui.screens.registration

import android.graphics.drawable.Icon
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.AppBar

@Composable
fun RegistrationScreen(
    viewModel: RegistrationViewModel,
    navController: NavHostController,
    onRegistrationSuccess: () -> Unit = {}
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // Navigazione in caso di successo
    LaunchedEffect(state.registrationSuccess) {
        if (state.registrationSuccess) {
            onRegistrationSuccess()
            viewModel.handleAction(RegistrationActions.ResetSuccess)
        }
    }

    // Pulisci errore generale quando l'utente interagisce
    LaunchedEffect(state.username, state.matricola, state.email, state.password, state.confirmPassword) {
        if (state.generalError != null) {
            viewModel.handleAction(RegistrationActions.ClearGeneralError)
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                title = "Registrazione",
                navController = navController,
                showSearchButton = false
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { contentPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Campo Nome Utente
            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.Person, "User")
                },
                value = state.username,
                onValueChange = { viewModel.handleAction(RegistrationActions.UpdateUsername(it)) },
                label = { Text("Nome Utente") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Numero Matricola
            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.AccountBox, "User")
                },
                value = state.matricola,
                onValueChange = { viewModel.handleAction(RegistrationActions.UpdateMatricola(it)) },
                label = { Text("Numero Matricola") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                singleLine = true,
                isError = state.matricolaError != null,
                supportingText = {
                    if (state.matricolaError != null) {
                        Text(
                            text = state.matricolaError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    } else {
                        Text(
                            text = "Deve essere di 10 cifre",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Email Istituzionale
            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.MailOutline, "User")
                },
                value = state.email,
                onValueChange = { viewModel.handleAction(RegistrationActions.UpdateEmail(it)) },
                label = { Text("Email Istituzionale") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                isError = state.emailError != null,
                supportingText = {
                    if (state.emailError != null) {
                        Text(
                            text = state.emailError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    } else {
                        Text(
                            text = "Deve terminare con @studio.unibo.it",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Password
            var passwordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.Lock, "User")
                },
                value = state.password,
                onValueChange = { viewModel.handleAction(RegistrationActions.UpdatePassword(it)) },
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = state.passwordError != null,
                supportingText = {
                    when {
                        state.passwordError != null -> {
                            Text(
                                text = state.passwordError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontSize = 12.sp
                            )
                        }
                        state.password.isNotEmpty() && state.password.length < 8 -> {
                            Text(
                                text = "⏳ Almeno 8 caratteri",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        state.password.isNotEmpty() -> {
                            Text(
                                text = buildString {
                                    append(if (state.password.any { it.isUpperCase() }) "✓ " else "○ ")
                                    append("Maiuscola  ")
                                    append(if (state.password.any { it.isLowerCase() }) "✓ " else "○ ")
                                    append("Minuscola  ")
                                    append(if (state.password.any { it.isDigit() }) "✓ " else "○ ")
                                    append("Numero  ")
                                    append(if (state.password.any { it in "!@#$%^&*()_+-=[]{}|;:,.<>?/~" }) "✓" else "○")
                                    append(" Carattere speciale")
                                },
                                fontSize = 11.sp,
                                color = if (state.passwordError == null && state.password.length >= 8)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        else -> {
                            Text(
                                text = "Almeno 8 caratteri, una maiuscola, una minuscola, un numero e un carattere speciale",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                },
                trailingIcon = {
                    TextButton(
                        onClick = { passwordVisible = !passwordVisible },
                        content = { Text(if (passwordVisible) "Nascondi" else "Mostra", fontSize = 12.sp) }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Campo Conferma Password
            var confirmPasswordVisible by remember { mutableStateOf(false) }

            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.CheckCircle, "User")
                },
                value = state.confirmPassword,
                onValueChange = { viewModel.handleAction(RegistrationActions.UpdateConfirmPassword(it)) },
                label = { Text("Conferma Password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                singleLine = true,
                isError = state.confirmPasswordError != null,
                supportingText = {
                    if (state.confirmPasswordError != null) {
                        Text(
                            text = state.confirmPasswordError!!,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 12.sp
                        )
                    }
                },
                trailingIcon = {
                    TextButton(
                        onClick = { confirmPasswordVisible = !confirmPasswordVisible },
                        content = { Text(if (confirmPasswordVisible) "Nascondi" else "Mostra", fontSize = 12.sp) }
                    )
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Checkbox Termini e condizioni
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = state.acceptedTerms,
                    onCheckedChange = { viewModel.handleAction(RegistrationActions.UpdateAcceptedTerms(it)) }
                )
                Text(
                    text = "Accetto i Termini di Servizio e la Privacy Policy",
                    fontSize = 14.sp,
                    modifier = Modifier.clickable {
                        viewModel.handleAction(RegistrationActions.UpdateAcceptedTerms(!state.acceptedTerms))
                    }
                )
            }

            if (state.termsError != null) {
                Text(
                    text = state.termsError!!,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Messaggio errore generale
            if (state.generalError != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = state.generalError!!,
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(12.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Bottone Registrati
            Button(
                onClick = { viewModel.handleAction(RegistrationActions.Register) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(12.dp),
                enabled = !state.isLoading
            ) {
                if (state.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Registrati", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}