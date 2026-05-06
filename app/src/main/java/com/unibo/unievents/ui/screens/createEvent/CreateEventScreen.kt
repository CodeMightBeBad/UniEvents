package com.unibo.unievents.ui.screens.createEvent

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.text.input.KeyboardType
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.TopBar

@Composable
fun CreateEventScreen(
    state: CreateEventState,
    actions: CreateEventActions,
    navController: NavHostController
) {
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
                        leadingIcon = {
                            Icon(Icons.Filled.DateRange, "data")
                        },
                        label = { Text("Inserisci la data*") },
                        value = state.date,
                        onValueChange = actions.updateDate,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.AccessTime, "ora")
                        },
                        label = { Text("Inserisci l'ora*") },
                        value = state.time,
                        onValueChange = actions.updateTime,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                }
            }

            OutlinedTextField(
                leadingIcon = {
                    Icon(Icons.Filled.Place, "indirizzo")
                },
                label = { Text("Indirizzo*") },
                value = state.address,
                onValueChange = actions.updateAddress,
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

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
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("RICHIEDI PUBBLICAZIONE")
                }

                OutlinedButton(
                    onClick = { },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ANNULLA")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
















/*
import android.Manifest
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.google.accompanist.permissions.*
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CreaEventoScreen(
    viewModel: CreaEventoViewModel = viewModel(),
    onEventCreated: () -> Unit,
    onCancel: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val result by viewModel.result.collectAsState()
    val context = LocalContext.current

    // Stato per l'URI della foto scattata
    var photoUri by remember { mutableStateOf<Uri?>(null) }

    // Permessi per fotocamera
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)

    // Permessi per storage (lettura galleria)
    val storagePermissionState = rememberPermissionState(
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }
    )

    // Launcher per selezionare immagini dalla galleria (multipla)
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris: List<Uri>? ->
        uris?.let { viewModel.addImages(it) }
    }

    // Launcher per aprire la galleria (singola)
    val singleImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let { viewModel.addSingleImage(it) }
    }

    // Launcher per scattare foto
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            photoUri?.let { uri ->
                viewModel.addSingleImage(uri)
            }
        }
        photoUri = null
    }

    // Crea file temporaneo per la foto
    fun createImageFile(): File {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val imageFileName = "JPEG_${timeStamp}_"
        val storageDir = context.cacheDir
        return File.createTempFile(imageFileName, ".jpg", storageDir)
    }

    // Dialog per scegliere tra fotocamera e galleria
    if (uiState.showImageSourceDialog) {
        AlertDialog(
            onDismissRequest = { viewModel.hideImageSourceDialog() },
            title = { Text("Aggiungi foto") },
            text = { Text("Scegli come aggiungere una foto") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.hideImageSourceDialog()
                        // Verifica permessi fotocamera
                        when {
                            cameraPermissionState.status.isGranted -> {
                                val file = createImageFile()
                                val uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.fileprovider",
                                    file
                                )
                                photoUri = uri
                                cameraLauncher.launch(uri)
                            }
                            cameraPermissionState.status.shouldShowRationale -> {
                                Toast.makeText(context, "Serve il permesso per usare la fotocamera", Toast.LENGTH_SHORT).show()
                                cameraPermissionState.launchPermissionRequest()
                            }
                            else -> {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CameraAlt, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scatta foto")
                    }
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        viewModel.hideImageSourceDialog()
                        // Verifica permessi galleria
                        when {
                            storagePermissionState.status.isGranted -> {
                                singleImageLauncher.launch("image/*")
                            }
                            storagePermissionState.status.shouldShowRationale -> {
                                Toast.makeText(context, "Serve il permesso per accedere alla galleria", Toast.LENGTH_SHORT).show()
                                storagePermissionState.launchPermissionRequest()
                            }
                            else -> {
                                storagePermissionState.launchPermissionRequest()
                            }
                        }
                    }
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.PhotoLibrary, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Scegli dalla galleria")
                    }
                }
            }
        )
    }

    // Gestione risultato
    LaunchedEffect(result) {
        when (result) {
            is CreateEventResult.Success -> {
                Toast.makeText(context, "Evento creato con successo!", Toast.LENGTH_LONG).show()
                onEventCreated()
                viewModel.resetForm()
            }
            is CreateEventResult.Error -> {
                Toast.makeText(context, (result as CreateEventResult.Error).message, Toast.LENGTH_SHORT).show()
            }
            null -> {}
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Crea un Evento",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Indietro")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF333333)
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Color(0xFFF5F5F5))
                .verticalScroll(rememberScrollState())
        ) {
            // Avviso revisione
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF3E0))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "Info",
                        tint = Color(0xFFFF9800)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Il tuo evento verrà revisionato da un amministratore prima di essere pubblicato",
                        fontSize = 12.sp,
                        color = Color(0xFFE65100)
                    )
                }
            }

            // Sezione Immagini
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Immagini Evento",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    // Pulsante Aggiungi Foto (apre dialog)
                    OutlinedButton(
                        onClick = { viewModel.showImageSourceDialog() },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Aggiungi")
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("AGGIUNGI FOTO")
                    }

                    // Preview immagini
                    if (uiState.images.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(12.dp))
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(uiState.images) { imageUri ->
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                ) {
                                    AsyncImage(
                                        model = ImageRequest.Builder(LocalContext.current)
                                            .data(imageUri)
                                            .crossfade(true)
                                            .build(),
                                        contentDescription = "Event image",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                    IconButton(
                                        onClick = { viewModel.removeImage(imageUri) },
                                        modifier = Modifier
                                            .align(Alignment.TopEnd)
                                            .size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Close,
                                            contentDescription = "Rimuovi",
                                            tint = Color.White,
                                            modifier = Modifier.background(
                                                Color.Black.copy(alpha = 0.6f),
                                                shape = RoundedCornerShape(50)
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Form Evento (stesso di prima)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.Title, "titolo")
                        },
                        value = uiState.title,
                        onValueChange = { viewModel.updateTitle(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Titolo Evento *") },
                        placeholder = { Text("Inserisci il titolo") },
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.title.isBlank() && uiState.errorMessage != null
                    )

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.Abc, "descrizione")
                        },
                        value = uiState.description,
                        onValueChange = { viewModel.updateDescription(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Descrizione *") },
                        placeholder = { Text("Descrivi l'evento") },
                        minLines = 3,
                        maxLines = 5,
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.description.isBlank() && uiState.errorMessage != null
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        OutlinedTextField(
                            leadingIcon = {
                                Icon(Icons.Filled.DateRange, "data")
                            },
                            value = uiState.date,
                            onValueChange = { viewModel.updateDate(it) },
                            modifier = Modifier.weight(1f),
                            label = { Text("Data *") },
                            placeholder = { Text("gg/mm/aaaa") },
                            shape = RoundedCornerShape(12.dp),
                            isError = uiState.date.isBlank() && uiState.errorMessage != null
                        )

                        OutlinedTextField(
                            leadingIcon = {
                                Icon(Icons.Filled.AccessTime, "ora")
                            },
                            value = uiState.time,
                            onValueChange = { viewModel.updateTime(it) },
                            modifier = Modifier.weight(1f),
                            label = { Text("Orario *") },
                            placeholder = { Text("HH:MM") },
                            shape = RoundedCornerShape(12.dp),
                            isError = uiState.time.isBlank() && uiState.errorMessage != null
                        )
                    }

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.AccountBalance, "luogo")
                        },
                        value = uiState.venueName,
                        onValueChange = { viewModel.updateVenueName(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Nome del Luogo *") },
                        placeholder = { Text("Es. Aula Magna, Sala Congressi") },
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.venueName.isBlank() && uiState.errorMessage != null
                    )

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.Place, "indirizzo")
                        },
                        value = uiState.address,
                        onValueChange = { viewModel.updateAddress(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Indirizzo *") },
                        placeholder = { Text("Via/Piazza, numero") },
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.address.isBlank() && uiState.errorMessage != null
                    )

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.Business, "città")
                        },
                        value = uiState.city,
                        onValueChange = { viewModel.updateCity(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Città *") },
                        placeholder = { Text("Es. Bologna") },
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.city.isBlank() && uiState.errorMessage != null
                    )

                    OutlinedTextField(
                        leadingIcon = {
                            Icon(Icons.Filled.Person, "persone")
                        },
                        value = uiState.maxParticipants,
                        onValueChange = { viewModel.updateMaxParticipants(it) },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Numero Massimo Partecipanti *") },
                        placeholder = { Text("50") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        shape = RoundedCornerShape(12.dp),
                        isError = uiState.maxParticipants.isBlank() && uiState.errorMessage != null,
                        trailingIcon = {
                            if (uiState.maxParticipants.isNotBlank()) {
                                Text(
                                    text = "max 1000",
                                    fontSize = 12.sp,
                                    color = Color.Gray
                                )
                            }
                        }
                    )

                    if (uiState.errorMessage != null) {
                        Text(
                            text = uiState.errorMessage!!,
                            fontSize = 12.sp,
                            color = Color.Red
                        )
                    }
                }
            }

            // Pulsanti
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("ANNULLA")
                }

                Button(
                    onClick = { viewModel.onSubmitClick() },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(12.dp),
                    enabled = uiState.isFormValid && !uiState.isSubmitting,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    )
                ) {
                    if (uiState.isSubmitting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            color = Color.White,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Text("RICHIEDI PUBBLICAZIONE")
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
*/