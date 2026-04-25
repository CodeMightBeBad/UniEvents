package com.unibo.unievents.ui.screens.map

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.TopBar

@Composable
fun MapEventsScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = { TopBar(navController, "Bacheca") },
        bottomBar = { BottomBar(navController) },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Titolo
            Text(
                text = "Mappa Eventi",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )

            // Mappa grafica
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "Mappa da aggiungere"
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Mappa Interattiva",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "Visualizza i luoghi degli eventi universitari",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            HorizontalDivider(
                color = MaterialTheme.colorScheme.outlineVariant,
                thickness = 1.dp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Eventi per località",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Laboratorio Informatica",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Via Mura Anteo Zamboni 7, Bologna",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "1 evento",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Sala Feste Campus",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Via Belmeloro 14, Bologna",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Text(
                            text = "1 evento",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/*
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.accompanist.permissions.*
import com.google.android.gms.location.LocationServices
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MapScreen(
    viewModel: MapViewModel = viewModel()
){
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val scope = rememberCoroutineScope()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Configurazione OSMDroid
    remember {
        Configuration.getInstance().load(
            context,
            androidx.preference.PreferenceManager.getDefaultSharedPreferences(context)
        )
    }

    // Permessi per la posizione
    val locationPermissionState = rememberPermissionState(Manifest.permission.ACCESS_FINE_LOCATION)

    LaunchedEffect(Unit) {
        when {
            locationPermissionState.status.isGranted -> {
                viewModel.updatePermissionStatus(true)
                // Ottieni l'ultima posizione nota
                getLastKnownLocation(context) { lat, lon ->
                    viewModel.updateUserLocation(lat, lon)
                }
            }
            locationPermissionState.status.shouldShowRationale -> {
                // Mostra spiegazione
            }
            else -> {
                locationPermissionState.launchPermissionRequest()
            }
        }
    }

    // Aggiorna stato permessi
    LaunchedEffect(locationPermissionState.status.isGranted) {
        viewModel.updatePermissionStatus(locationPermissionState.status.isGranted)
        if (locationPermissionState.status.isGranted) {
            getLastKnownLocation(context) { lat, lon ->
                viewModel.updateUserLocation(lat, lon)
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF6200EE))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Mappa Eventi",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                if (uiState.userLocation != null) {
                    Icon(
                        imageVector = Icons.Default.MyLocation,
                        contentDescription = "La mia posizione",
                        tint = Color.White,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                // Centra sulla posizione
                            }
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                // Mappa (60% dello schermo)
                Box(
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxHeight()
                ) {
                    if (locationPermissionState.status.isGranted) {
                        OSMMapView(
                            events = uiState.events,
                            selectedEvent = uiState.selectedEvent,
                            userLocation = uiState.userLocation,
                            onMarkerClick = { event ->
                                viewModel.selectEvent(event)
                            }
                        )
                    } else {
                        // Richiesta permessi
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("Permesso di posizione necessario")
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { locationPermissionState.launchPermissionRequest() }
                                ) {
                                    Text("Concedi permesso")
                                }
                            }
                        }
                    }
                }

                // Lista eventi (40% dello schermo)
                Column(
                    modifier = Modifier
                        .weight(0.4f)
                        .fillMaxHeight()
                        .background(Color(0xFFF5F5F5))
                ) {
                    Text(
                        text = "Eventi per località",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333),
                        modifier = Modifier.padding(12.dp)
                    )

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(uiState.events) { event ->
                            EventLocationCard(
                                event = event,
                                isSelected = uiState.selectedEvent?.id == event.id,
                                onClick = {
                                    viewModel.selectEvent(event)
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OSMMapView(
    events: List<EventLocation>,
    selectedEvent: EventLocation?,
    userLocation: GeoPoint?,
    onMarkerClick: (EventLocation) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            setTileSource(TileSourceFactory.MAPNIK)
            setMultiTouchControls(true)
            // zoomController.setVisibility(org.osmdroid.views.MapView.ZoomControllerVisibility.SHOW_AND_FADE_OUT)
            setBuiltInZoomControls(true)
        }
    }

    // Aggiungi overlay posizione utente
    val myLocationOverlay = remember {
        MyLocationNewOverlay(GpsMyLocationProvider(context), mapView).apply {
            enableMyLocation()
            enableFollowLocation()
            isDrawAccuracyEnabled = true
        }
    }

    // Aggiungi marker per ogni evento
    val markers = remember { mutableStateListOf<Marker>() }

    LaunchedEffect(events) {
        // Rimuovi marker esistenti
        markers.forEach { mapView.overlays.remove(it) }
        markers.clear()

        // Crea nuovi marker
        events.forEach { event ->
            val marker = Marker(mapView).apply {
                position = GeoPoint(event.latitude, event.longitude)
                title = event.title
                setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
                // setInfoWindow(org.osmdroid.views.overlay.infowindow.BasicInfoWindow(
                //     org.osmdroid.library.R.layout.basic_info_window, mapView
                // ))

                // Icona personalizzata
                // icon = ContextCompat.getDrawable(context, R.drawable.ic_marker)

                setOnMarkerClickListener { _, _ ->
                    onMarkerClick(event)
                    true
                }
            }
            mapView.overlays.add(marker)
            markers.add(marker)
        }
    }

    // Centra sulla posizione dell'evento selezionato
    LaunchedEffect(selectedEvent) {
        if (selectedEvent != null) {
            val geoPoint = GeoPoint(selectedEvent.latitude, selectedEvent.longitude)
            mapView.controller.animateTo(geoPoint)
            mapView.controller.setZoom(17.0)
        }
    }

    // Centra sulla posizione utente se disponibile e nessun evento selezionato
    LaunchedEffect(userLocation, selectedEvent) {
        if (selectedEvent == null && userLocation != null) {
            mapView.controller.animateTo(userLocation)
            mapView.controller.setZoom(15.0)
        }
    }

    // Aggiungi overlay posizione
    LaunchedEffect(Unit) {
        mapView.overlays.add(myLocationOverlay)
        myLocationOverlay.enableMyLocation()
    }

    // Gestione ciclo di vita
    val lifecycleObserver = remember {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                else -> {}
            }
        }
    }

    // DisposableEffect(Unit) {
    // val lifecycle = LocalLifecycleOwner.current.lifecycle
    // lifecycle.addObserver(lifecycleObserver)
    // onDispose {
    //     lifecycle.removeObserver(lifecycleObserver)
    //    mapView.onDetach()
    //}
    //}

    AndroidView(
        factory = { mapView },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun EventLocationCard(
    event: EventLocation,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) Color(0xFFE3F2FD) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp)
        ) {
            Text(
                text = event.location,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF333333)
            )
            Text(
                text = event.address,
                fontSize = 11.sp,
                color = Color.Gray,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Event,
                    contentDescription = "Eventi",
                    modifier = Modifier.size(14.dp),
                    tint = Color(0xFF6200EE)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${event.title}",
                    fontSize = 12.sp,
                    color = Color(0xFF6200EE)
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Schedule,
                    contentDescription = "Data",
                    modifier = Modifier.size(12.dp),
                    tint = Color.Gray
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = "${event.date} - ${event.time}",
                    fontSize = 11.sp,
                    color = Color.Gray
                )
            }
        }
    }
}

// Funzione helper per ottenere l'ultima posizione nota
private fun getLastKnownLocation(
    context: Context,
    onResult: (Double, Double) -> Unit
) {
    try {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocationClient.lastLocation.addOnSuccessListener { location ->
                location?.let {
                    onResult(it.latitude, it.longitude)
                }
            }
        }
    } catch (e: SecurityException) {
        // Permesso negato
    }
}
*/
