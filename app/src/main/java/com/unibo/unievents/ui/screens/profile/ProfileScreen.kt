package com.unibo.unievents.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.TopBar
import com.unibo.unievents.utils.rememberCameraLauncher
import com.unibo.unievents.utils.uriToBitmap

@Composable
fun ProfileScreen(
    state: ProfileState,
    actions: ProfileActions,
    navController: NavHostController
) {
    var isEditing by remember { mutableStateOf(false) }
    var statsExpanded by remember { mutableStateOf(true) }

    val ctx = LocalContext.current
    val (picUri, takePicture) = rememberCameraLauncher(
        onPictureTaken = { uri ->
            val bitmap = uriToBitmap(uri, ctx.contentResolver)
            actions.uploadImage(bitmap)
        }
    )

    Scaffold(
        topBar = { TopBar(navController, if (isEditing) "Modifica Profilo" else "Il mio Profilo") },
        bottomBar = { BottomBar(navController)},
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (!isEditing) {
                Card(modifier = Modifier.fillMaxWidth()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.primary
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Surface(
                                modifier = Modifier.size(56.dp),
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = "Av",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                            Column {
                                Text(
                                    text = state.email,
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                                Text(
                                    text = "Matricola: ${state.badgeNumber}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            }
                        }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Filled.EmojiEvents, contentDescription = null)
                                Text(
                                    text = "Livello 0",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            AssistChip(
                                onClick = {},
                                label = { Text("0 punti") },
                                leadingIcon = {
                                    Icon(
                                        Icons.Filled.Star,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        LinearProgressIndicator(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Prossimo livello", style = MaterialTheme.typography.labelSmall)
                            Text("100 punti", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(Icons.Filled.BarChart, contentDescription = null)
                                Text(
                                    text = "Le mie Statistiche",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                            IconButton(onClick = { statsExpanded = !statsExpanded }) {
                                Icon(
                                    imageVector = if (statsExpanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                                    contentDescription = if (statsExpanded) "Comprimi" else "Espandi"
                                )
                            }
                        }

                        AnimatedVisibility(
                            visible = statsExpanded,
                            enter = expandVertically(),
                            exit = shrinkVertically()
                        ) {
                            Column {
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    StatCard(
                                        modifier = Modifier.weight(1f),
                                        icon = { Icon(Icons.Filled.CalendarMonth, contentDescription = null) },
                                        value = "0",
                                        label = "Eventi Partecipati"
                                    )
                                    StatCard(
                                        modifier = Modifier.weight(1f),
                                        icon = { Icon(Icons.Filled.EmojiEvents, contentDescription = null) },
                                        value = "0",
                                        label = "Eventi Creati"
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                                ) {
                                    StatCard(
                                        modifier = Modifier.weight(1f),
                                        icon = { Icon(Icons.Filled.Person, contentDescription = null) },
                                        value = "0",
                                        label = "Utenti Seguiti"
                                    )
                                    StatCard(
                                        modifier = Modifier.weight(1f),
                                        icon = { Icon(Icons.Filled.Star, contentDescription = null) },
                                        value = "0",
                                        label = "Livello Raggiunto"
                                    )
                                }
                            }
                        }
                    }
                }

                Button(
                    onClick = { isEditing = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Modifica Profilo")
                }

            } else {

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Text("Foto Profilo", style = MaterialTheme.typography.labelLarge)

                        OutlinedButton(
                            onClick = takePicture,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.CameraAlt, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("CAMBIA FOTO PROFILO")
                        }

                        OutlinedTextField(
                            value = state.badgeNumber,
                            onValueChange = {},
                            leadingIcon = { Icon(Icons.Filled.AccountBox, contentDescription = null) },
                            label = { Text("Matricola") },
                            enabled = false,
                            supportingText = { Text("La matricola non può essere modificata") },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = "mario.rossi@studio.unibo.it",
                            leadingIcon = { Icon(Icons.Filled.MailOutline, contentDescription = null) },
                            label = { Text("Email") },
                            onValueChange = {},
                            enabled = false,
                            supportingText = { Text("L'email non può essere modificata") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Filled.Lock, contentDescription = null)
                            Text(
                                text = "Cambia Password",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedTextField(
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            label = { Text("Password Vecchia") },
                            value = state.oldPassword,
                            onValueChange = actions.updatePassword,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = PasswordVisualTransformation(),
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            leadingIcon = { Icon(Icons.Filled.Lock, contentDescription = null) },
                            label = { Text("Password Nuova") },
                            value = state.newPassword,
                            onValueChange = actions.updateNewPassword,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            visualTransformation = PasswordVisualTransformation(),
                            supportingText = { Text("Almeno 8 caratteri") },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                Button(
                    onClick = { isEditing = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Filled.Edit, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SALVA MODIFICHE")
                }

                OutlinedButton(
                    onClick = { isEditing = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Outlined.Cancel, "Cancel")
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("ANNULLA")
                }
            }
        }
    }
}

@Composable
private fun StatCard(
    modifier: Modifier = Modifier,
    icon: @Composable () -> Unit,
    value: String,
    label: String
) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            icon()
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                textAlign = TextAlign.Center
            )
        }
    }
}