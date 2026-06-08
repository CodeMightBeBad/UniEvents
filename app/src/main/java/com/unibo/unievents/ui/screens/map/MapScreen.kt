package com.unibo.unievents.ui.screens.map

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.preference.PreferenceManager
import com.unibo.unievents.data.Coordinates
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.LocationService
import com.unibo.unievents.ui.PermissionDeniedAlert
import com.unibo.unievents.ui.PermissionPermanentlyDeniedSnackbar
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.TopBar
import com.unibo.unievents.ui.composables.parseItalianAddress
import com.unibo.unievents.ui.screens.research.formatEventDate
import com.unibo.unievents.utils.PermissionStatus
import com.unibo.unievents.utils.rememberMultiplePermissions
import kotlinx.coroutines.launch
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.infowindow.InfoWindow

@SuppressLint("ConfigurationScreenWidthHeight")
@Composable
fun MapScreen(
    state: MapState,
    actions: MapActions,
    navController: NavHostController
) {
    var showLocationDisabledAlert by remember { mutableStateOf(false) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    var showPermissionSnackbar by remember { mutableStateOf(false) }

    val ctx = LocalContext.current
    val locationService = remember { LocationService(ctx) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val coordinates by locationService.coordinates.collectAsStateWithLifecycle()
    val loadingLocation by locationService.isLoading.collectAsStateWithLifecycle()

    val conf = LocalConfiguration.current
    val isLandscape = conf.screenWidthDp > conf.screenHeightDp

    fun getCurrentLocation() = scope.launch {
        try {
            locationService.getCurrentLocation()
        } catch (_: IllegalStateException) {
            showLocationDisabledAlert = true
        }
    }

    val locationPermissions = rememberMultiplePermissions(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    ) { statuses ->
        when {
            statuses.any { it.value == PermissionStatus.Granted } -> getCurrentLocation()
            statuses.all { it.value == PermissionStatus.PermanentlyDenied } -> showPermissionSnackbar = true
            else -> showPermissionDeniedAlert = true
        }
    }

    fun getLocationOrRequestPermission() {
        if (locationPermissions.statuses.any { it.value == PermissionStatus.Granted }) getCurrentLocation()
        else locationPermissions.launchPermissionRequest()
    }

    fun openLocationSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }

        if (intent.resolveActivity(ctx.packageManager) != null) ctx.startActivity(intent)
    }

    if (showLocationDisabledAlert) {
        LocationDisabledAlert(
            onAction = { openLocationSettings() },
            onDismiss = { showLocationDisabledAlert = false }
        )
    }

    if (showPermissionDeniedAlert) {
        PermissionDeniedAlert(
            onAction = { locationPermissions.launchPermissionRequest() },
            onDismiss = { showPermissionDeniedAlert = false },
            title = { Text("Accesso alla posizione negato") },
            text = { Text("L'accesso alla posizione deve essere concesso per poter ricavare la tua posizione attuale") }
        )
    }

    if (showPermissionSnackbar) {
        PermissionPermanentlyDeniedSnackbar(
            snackbarHostState = snackbarHostState,
            onAction = {
                val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", ctx.packageName, null)
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                if (intent.resolveActivity(ctx.packageManager) != null) {
                    ctx.startActivity(intent)
                }
            },
            onHide = { showPermissionSnackbar = false },
            message = "Attiva i permessi per la posizione"
        )
    }

    Scaffold(
        topBar = { TopBar(navController, "Mappa eventi") },
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        if (isLandscape) {
            Row (
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Box {
                        EventsMap(state.events, state.selectedEvent, coordinates)
                        IconButton(
                            onClick = {
                                actions.clearSelectedEvent()
                                getLocationOrRequestPermission()
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            enabled = !loadingLocation
                        ) {
                            Icon(Icons.Filled.GpsFixed, "Current location")
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight()
                ) {
                    Text(
                        text = "Eventi:",
                        style = MaterialTheme.typography.headlineLarge,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    if (state.events.isEmpty()) {
                        Text(
                            text = "Non ci sono eventi in programma",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            items(state.events) { event ->
                                EventCard(
                                    event = event,
                                    onClick = {
                                        actions.selectEvent(
                                            event.latitude.toDouble(),
                                            event.longitude.toDouble(),
                                        )
                                    },
                                    enabled = !loadingLocation
                                )
                            }
                        }
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(15.dp)
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(360.dp)
                ) {
                    Box {
                        EventsMap(state.events, state.selectedEvent, coordinates)

                        IconButton(
                            onClick = {
                                actions.clearSelectedEvent()
                                getLocationOrRequestPermission()
                            },
                            colors = IconButtonDefaults.iconButtonColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer,
                                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            ),
                            enabled = !loadingLocation
                        ) {
                            if (loadingLocation) {
                                CircularProgressIndicator()
                            } else {
                                Icon(Icons.Filled.GpsFixed, "Current location")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Eventi:",
                    style = MaterialTheme.typography.headlineLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(10.dp))
                if (state.events.isEmpty()) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                    ) {
                        Text(
                            text = "Non ci sono eventi in programma",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        items(state.events) { event ->
                            EventCard(
                                event = event,
                                onClick = {
                                    actions.selectEvent(
                                        event.latitude.toDouble(),
                                        event.longitude.toDouble()
                                    )
                                },
                                enabled = !loadingLocation
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EventsMap(
    events: List<Event>,
    selectedEvent: Pair<Double, Double>?,
    userLocation: Coordinates?
) {
    val ctx = LocalContext.current
    var lastSelection by remember { mutableStateOf<Pair<Double, Double>?>(null) }
    var lastUserLocation by remember { mutableStateOf<Coordinates?>(null) }

    // Don't run this code at every recomposition, only once
    LaunchedEffect(Unit) {
        Configuration.getInstance().load(
            ctx.applicationContext,
            PreferenceManager.getDefaultSharedPreferences(ctx.applicationContext)
        )
    }

    AndroidView(
        factory = { ctx ->
            MapView(ctx).apply {
                setTileSource(TileSourceFactory.MAPNIK)
                setMultiTouchControls(true)

                zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                controller.setZoom(17.0)

                overlays.add(org.osmdroid.views.overlay.MapEventsOverlay(
                    object : org.osmdroid.events.MapEventsReceiver {
                        override fun singleTapConfirmedHelper(p: GeoPoint?): Boolean {
                            InfoWindow.closeAllInfoWindowsOn(this@apply)
                            invalidate()
                            return true
                        }
                        override fun longPressHelper(p: GeoPoint?) = false
                    }
                ))

                if (selectedEvent != null) {
                    controller.setCenter(GeoPoint(selectedEvent.first, selectedEvent.second))
                    lastSelection = selectedEvent
                } else if (userLocation != null) {
                    controller.setCenter(GeoPoint(userLocation.latitude, userLocation.longitude))
                    lastUserLocation = userLocation
                } else {
                    controller.setCenter(GeoPoint(44.14771, 12.2356534))
                }
            }
        },
        update = { mapView ->
            mapView.overlays.removeAll { it is Marker }

            events.forEach { event ->
                val (city, street) = parseItalianAddress(event.address)
                val marker = Marker(mapView).apply {
                    position = GeoPoint(event.latitude.toDouble(), event.longitude.toDouble())
                    title = event.title
                    snippet = "$city, $street"
                }

                mapView.overlays.add(marker)
            }

            if (selectedEvent != null) {
                if (selectedEvent != lastSelection) {
                    mapView.controller.animateTo(GeoPoint(selectedEvent.first, selectedEvent.second))
                    lastSelection = selectedEvent
                    lastUserLocation = null
                }
            } else {
                lastSelection = null
                if (userLocation != null && userLocation != lastUserLocation) {
                    mapView.controller.animateTo(GeoPoint(userLocation.latitude, userLocation.longitude))
                    lastUserLocation = userLocation
                }
            }

            mapView.invalidate()
        }
    )
}

@Composable
fun EventCard(
    event: Event,
    onClick: () -> Unit,
    enabled: Boolean
) {
    val (city, street) = remember(event.address) {
        parseItalianAddress(event.address)
    }
    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        onClick = onClick,
        enabled = enabled
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = event.title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "$city, $street",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${formatEventDate(event.date)} alle ore ${event.time}",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (event.maxParticipants != null) {
                    "Partecipanti: ${event.currentParticipants}/${event.maxParticipants}"
                } else {
                    "Partecipanti: ${event.currentParticipants}"
                },
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun LocationDisabledAlert(
    onAction: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        title = { Text("La posizione è disattivata") },
        text = { Text("La geolocalizzazione deve essere abilitata per poter ricavare la tua posizione attuale") },
        confirmButton = {
            TextButton(
                onClick = {
                    onAction()
                    onDismiss()
                }
            ) {
                Text("Attiva")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annulla")
            }
        },
        onDismissRequest = onDismiss
    )
}