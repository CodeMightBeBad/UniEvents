package com.unibo.unievents.ui.screens.profile

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
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
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Photo
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
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
import com.unibo.unievents.utils.rememberGalleryLauncher
import com.unibo.unievents.utils.uriToBitmap

@Composable
fun ProfileScreen(
    state: ProfileState,
    actions: ProfileActions,
    navController: NavHostController
) {
    var showDialog by remember { mutableStateOf(false) }
    val ctx = LocalContext.current

    val (_, takePicture) = rememberCameraLauncher(
        onPictureTaken = { imageUri ->
            val bitmap = uriToBitmap(imageUri, ctx.contentResolver)
            actions.updateProfilePicture(bitmap)

            actions.toggleEdit()
        }
    )

    val galleryPick = rememberGalleryLauncher(
        onImagePicked = { imageUri ->
            val bitmap = uriToBitmap(imageUri, ctx.contentResolver)
            actions.updateProfilePicture(bitmap)

            actions.toggleEdit()
        }
    )

    if (showDialog) {
        PictureSelectionDialog(
            takePicture = takePicture,
            selectPicture = galleryPick,
            onDismiss = { showDialog = false }
        )
    }

    Scaffold(
        topBar = { TopBar(navController, "Il mio profilo") },
        bottomBar = { BottomBar(navController) },
        containerColor = MaterialTheme.colorScheme.surface
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(15.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            if (state.editing) {
                EditProfile(
                    state = state,
                    actions = actions,
                    onChangePicture = { showDialog = true }
                )
            } else {
                UserInformationCard(
                    state = state,
                    onEdit = actions.toggleEdit
                )
            }
        }
    }
}

@Composable
private fun UserInformationCard(
    state: ProfileState,
    onEdit: () -> Unit
) {
    var statsExpanded by remember { mutableStateOf(true) }

    // Profile card
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(15.dp)
        ) {
            Column {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceBright,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        if (state.loadingImage) {
                            CircularProgressIndicator()
                        } else {
                            if (state.profilePicture == null) {
                                Icon(Icons.Filled.Person, "Profile picture")
                            } else {
                                Image(
                                    bitmap = state.profilePicture.asImageBitmap(),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "Profile picture"
                                )
                            }
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                Text(
                    text = state.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "Matricola: ${state.badgeNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = onEdit) {
                Icon(
                    Icons.Outlined.Edit,
                    contentDescription = "Edit profile",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Modifica Password o Foto Profilo")
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
    }

    // Level card
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

            Spacer(modifier = Modifier.height(6.dp))

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

    // Stats card
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
                            value = state.joinedEvents.toString(),
                            label = "Eventi Partecipati"
                        )
                        StatCard(
                            modifier = Modifier.weight(1f),
                            icon = { Icon(Icons.Filled.EmojiEvents, contentDescription = null) },
                            value = state.createdEvents.toString(),
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
                            value = state.friends.toString(),
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
}

@Composable
private fun EditProfile(
    state: ProfileState,
    actions: ProfileActions,
    onChangePicture: () -> Unit
) {
    // Profile card
    Card(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp),
            modifier = Modifier.padding(15.dp)
        ) {
            Column {
                Surface(
                    shape = CircleShape,
                    color = MaterialTheme.colorScheme.surfaceBright,
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        IconButton(onClick = onChangePicture) {
                            Icon(Icons.Outlined.CameraAlt, "Change picture")
                        }
                    }
                }
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(10.dp)
            ) {
                Text(
                    text = state.email,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                    text = "Matricola: ${state.badgeNumber}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.secondary
                )
            }
        }

        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(onClick = actions.toggleEdit) {
                Icon(
                    Icons.Outlined.Close,
                    contentDescription = "Go back",
                    modifier = Modifier.size(ButtonDefaults.IconSize)
                )
                Spacer(modifier = Modifier.size(ButtonDefaults.IconSpacing))
                Text(text = "Torna alle Statistiche")
            }
        }
        Spacer(modifier = Modifier.height(18.dp))
    }

    // Change password card
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
                    Icon(Icons.Filled.Lock, contentDescription = null)
                    Text(
                        text = "Cambia Password",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
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
                supportingText = { Text("Almeno 8 caratteri, una lettera maiuscola, una lettera minuscola, un numero e un carattere speciale") },
                modifier = Modifier.fillMaxWidth()
            )
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PictureSelectionDialog(
    takePicture: () -> Unit,
    selectPicture: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text("Cambia foto profilo") },
        text = { Text("Scegli da dove vuoi caricare la foto") },
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = {
                takePicture()
                onDismiss()
            }) {
                Icon(Icons.Outlined.CameraAlt, "Camera")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Scatta foto")
            }
        },
        dismissButton = {
            TextButton(onClick = {
                selectPicture()
                onDismiss()
            }) {
                Icon(Icons.Outlined.Photo, "Gallery")
                Spacer(modifier = Modifier.width(6.dp))
                Text("Scegli foto")
            }
        }
    )
}