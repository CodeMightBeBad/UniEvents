package com.unibo.unievents.ui.screens.login

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.TopBar
import androidx.compose.material.icons.filled.*
import com.unibo.unievents.ui.NavigationRoute

@Composable
fun LoginScreen(
    state: LoginState,
    actions: LoginActions,
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopBar(navController, "Login")
        },
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
                        modifier = Modifier.padding(horizontal = 16.dp)
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.Badge, "matricola")
                        },
                        value = state.email,
                        onValueChange = actions.updateEmail,
                        placeholder = { Text("Email") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.Lock, "password")
                        },
                        value = state.password,
                        onValueChange = actions.updatePassword,
                        placeholder = { Text("Password") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        singleLine = true,
                        visualTransformation = PasswordVisualTransformation()
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Button(
                        onClick = actions.confirm,
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

                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TextButton(
                        onClick = { },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Privacy",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    TextButton(
                        onClick = { },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = "Termini",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    TextButton(
                        onClick = { },
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



/*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.navigation.NavHostController

@Composable
fun LoginScreen(
    navController: NavHostController,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val loginResult by viewModel.loginResult.collectAsState()

    // Gestione risultato login
    LaunchedEffect(loginResult) {
        when (loginResult) {
            is LoginResult.Success -> {
                navController.navigate("home") // o NavigationRoute.Home
            }
            is LoginResult.Error -> {
                // L'errore è già nello uiState
            }
            null -> {}
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                // Header
                Text(
                    text = "Uni Events",
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF6200EE)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "La piattaforma per scoprire e partecipare\nagli eventi universitari dell'Università di Bologna",
                    fontSize = 14.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Card del form
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Campo Matricola
                        Text(
                            text = "Numero Matricola",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333),
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = uiState.matricola,
                            onValueChange = { viewModel.updateMatricola(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("0000123456") },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            isError = uiState.errorMessage != null && uiState.matricola.isNotEmpty() && uiState.matricola.length != 10,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6200EE),
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Campo Password
                        Text(
                            text = "Password",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF333333),
                            modifier = Modifier.align(Alignment.Start)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = { viewModel.updatePassword(it) },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = { Text("********") },
                            shape = RoundedCornerShape(12.dp),
                            singleLine = true,
                            visualTransformation = if (uiState.isPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            isError = uiState.errorMessage != null && uiState.password.isNotEmpty() && uiState.password.length < 6,
                            trailingIcon = {
                                IconButton(onClick = { viewModel.togglePasswordVisibility() }) {
                                    Icon(
                                        imageVector = if (uiState.isPasswordVisible) {
                                            Icons.Default.VisibilityOff
                                        } else {
                                            Icons.Default.Visibility
                                        },
                                        contentDescription = if (uiState.isPasswordVisible) "Nascondi password" else "Mostra password",
                                        tint = Color.Gray
                                    )
                                }
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = Color(0xFF6200EE),
                                unfocusedBorderColor = Color(0xFFE0E0E0)
                            )
                        )

                        // Messaggio errore
                        if (uiState.errorMessage != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = uiState.errorMessage!!,
                                fontSize = 12.sp,
                                color = Color.Red,
                                modifier = Modifier.align(Alignment.Start)
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Bottone Accedi
                        Button(
                            onClick = { viewModel.onLoginClick() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            // enabled = uiState.isLoginEnabled && !uiState.isLoading,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF6200EE),
                                disabledContainerColor = Color(0xFFE0E0E0)
                            )
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(24.dp),
                                    color = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    text = "Accedi",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Oppure
                        Text(
                            text = "oppure",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Bottone Registrati ora
                        OutlinedButton(
                            onClick = { },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = Color(0xFF6200EE)
                            )
                        ) {
                            Text(
                                text = "Registrati ora",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Password dimenticata
                        Text(
                            text = "Password dimenticata?",
                            fontSize = 14.sp,
                            color = Color(0xFF6200EE),
                            modifier = Modifier
                                .clickable { viewModel.onForgotPasswordClick() }
                                .padding(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Esplora eventi senza accedere
                Text(
                    text = "Esplora eventi senza accedere →",
                    fontSize = 14.sp,
                    color = Color(0xFF6200EE),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .clickable { }
                        .padding(8.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Footer
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "© 2028 Università di Bologna - Tutti i diritti riservati",
                        fontSize = 11.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text(
                            text = "Privacy",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.clickable { }
                        )
                        Text(
                            text = "Termini",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.clickable { }
                        )
                        Text(
                            text = "Contatti",
                            fontSize = 11.sp,
                            color = Color.Gray,
                            modifier = Modifier.clickable { }
                        )
                    }
                }
            }
        }
    }
}
 */