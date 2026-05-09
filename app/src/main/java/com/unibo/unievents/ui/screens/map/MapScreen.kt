package com.unibo.unievents.ui.screens.map

import android.Manifest
import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.PermissionDeniedAlert
import com.unibo.unievents.ui.PermissionPermanentlyDeniedSnackbar
import com.unibo.unievents.ui.LocationDisabledAlert
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.TopBar
import com.unibo.unievents.utils.PermissionStatus
import com.unibo.unievents.utils.rememberMultiplePermissions
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.views.CustomZoomButtonsController

@Composable
fun MapEventsScreen(
    navController: NavHostController,
    viewModel: MapViewModel
) {
    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    var showLocationDisabledAlert by remember { mutableStateOf(false) }
    var showPermissionDeniedAlert by remember { mutableStateOf(false) }
    var showPermissionPermanentlyDeniedSnackbar by remember { mutableStateOf(false) }
    var shouldGetLocationAfterPermission by remember { mutableStateOf(false) }

    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    var myLocationOverlayRef by remember { mutableStateOf<MyLocationNewOverlay?>(null) }
    var selectedLocation by remember { mutableStateOf<EventLoc?>(null) }

    var isMyLocationEnabled by remember { mutableStateOf(false) }

    val hasLocationPermission = ContextCompat.checkSelfPermission(
        context,
        Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED

    LaunchedEffect(hasLocationPermission) {
        if (hasLocationPermission) {
            shouldGetLocationAfterPermission = true
        }
    }

    val eventLocations = remember(uiState.events) {
        uiState.events.map { event ->
            EventLoc(
                name = event.location,
                address = event.address,
                eventCount = 1,
                geoPoint = GeoPoint(event.latitude, event.longitude),
                events = listOf(event.title)
            )
        }
    }

    val permissionHandler = rememberMultiplePermissions(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    ) { statuses ->
        viewModel.updatePermissionStatus(statuses)

        val fineLocationStatus = statuses[Manifest.permission.ACCESS_FINE_LOCATION]
        when (fineLocationStatus) {
            PermissionStatus.Denied -> showPermissionDeniedAlert = true
            PermissionStatus.PermanentlyDenied -> showPermissionPermanentlyDeniedSnackbar = true
            PermissionStatus.Granted -> shouldGetLocationAfterPermission = true
            else -> { }
        }
    }

    LaunchedEffect(shouldGetLocationAfterPermission) {
        if (shouldGetLocationAfterPermission) {
            try {
                viewModel.getCurrentLocation()
            } catch (e: IllegalStateException) {
                showLocationDisabledAlert = true
            }
            shouldGetLocationAfterPermission = false
        }
    }

    LaunchedEffect(Unit) {
        try {
            viewModel.getCurrentLocation()
        } catch (e: IllegalStateException) {
            showLocationDisabledAlert = true
        }
    }



    Scaffold(
        topBar = { TopBar(navController, "Mappa Eventi") },
        bottomBar = { BottomBar(navController) },
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, top = 3.dp, bottom = 3.dp, end = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {

                        if (uiState.currentLocation != null) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Filled.LocationOn,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = MaterialTheme.colorScheme.primary
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Posizione attuale: ${String.format("%.4f", uiState.currentLocation!!.latitude)}, ${String.format("%.4f", uiState.currentLocation!!.longitude)}",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        } else if (uiState.isLoadingLocation) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 4.dp)
                            ) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(12.dp),
                                    strokeWidth = 1.dp
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Ricerca posizione in corso...",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }

                    Row {
                        IconButton(
                            onClick = {
                                uiState.currentLocation?.let {
                                    mapViewRef?.controller?.animateTo(
                                        GeoPoint(it.latitude, it.longitude)
                                    )
                                    mapViewRef?.controller?.setZoom(17.0)
                                    myLocationOverlayRef?.enableFollowLocation()
                                }
                            },
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.9f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.MyLocation,
                                contentDescription = "Centra sulla mia posizione",
                                tint = if (uiState.currentLocation != null)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                if (uiState.isLoadingLocation && uiState.currentLocation == null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Card(
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant
                            )
                        ) {
                            Column(
                                modifier = Modifier.padding(24.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                CircularProgressIndicator()
                                Spacer(modifier = Modifier.height(12.dp))
                                Text(
                                    text = "Caricamento mappa...",
                                    style = MaterialTheme.typography.bodyMedium
                                )
                            }
                        }
                    }
                } else {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(360.dp)
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Box {
                            AndroidView(
                                factory = { ctx ->
                                    MapView(ctx).apply {
                                        Configuration.getInstance().load(
                                            ctx.applicationContext,
                                            androidx.preference.PreferenceManager.getDefaultSharedPreferences(ctx.applicationContext)
                                        )

                                        setTileSource(TileSourceFactory.MAPNIK)
                                        setMultiTouchControls(true)
                                        zoomController.setVisibility(CustomZoomButtonsController.Visibility.NEVER)

                                        val startPoint = uiState.currentLocation?.let {
                                            GeoPoint(it.latitude, it.longitude)
                                        } ?: GeoPoint(44.143333,12.249722) // cesena
                                        // (44.4949, 11.3426) // Bologna centro

                                        controller.setZoom(17.0)
                                        controller.setCenter(startPoint)

                                        val myLocationOverlay = MyLocationNewOverlay(
                                            GpsMyLocationProvider(ctx),
                                            this
                                        )

                                        myLocationOverlay.enableMyLocation()
                                        myLocationOverlay.enableFollowLocation()
                                        myLocationOverlay.isDrawAccuracyEnabled = true
                                        overlays.add(myLocationOverlay)
                                        myLocationOverlayRef = myLocationOverlay

                                        eventLocations.forEach { location ->
                                            val marker = Marker(this)
                                            marker.position = location.geoPoint
                                            marker.title = location.name
                                            marker.subDescription = "${location.eventCount} evento"
                                            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

                                            marker.setOnMarkerClickListener { _, _ ->
                                                selectedLocation = location
                                                controller.animateTo(location.geoPoint)
                                                marker.showInfoWindow()
                                                true
                                            }

                                            overlays.add(marker)
                                        }

                                        mapViewRef = this
                                    }
                                },
                                modifier = Modifier.fillMaxSize()
                            )

                            if (uiState.currentLocation != null && !uiState.isLoadingLocation) {
                                Box(
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(16.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.9f))
                                        .padding(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            imageVector = Icons.Filled.MyLocation,
                                            contentDescription = null,
                                            modifier = Modifier.size(14.dp),
                                            tint = MaterialTheme.colorScheme.primary
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = "Clicca il bottone sopra per sapere dove sei",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                if (!hasLocationPermission) {
                    Button(
                        onClick = { permissionHandler.launchPermissionRequest() },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Abilita posizione per vedere eventi vicini")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Text(
                    text = "Eventi per località",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (uiState.isLoading) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(eventLocations.size) { index ->
                            val location = eventLocations[index]

                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        selectedLocation = location
                                        mapViewRef?.let { mapView ->
                                            mapView.controller.animateTo(location.geoPoint)
                                            mapView.controller.setZoom(16.0)

                                            mapView.overlays.forEach { overlay ->
                                                if (overlay is Marker) {
                                                    if (overlay.position == location.geoPoint) {
                                                        overlay.showInfoWindow()
                                                    } else {
                                                        overlay.closeInfoWindow()
                                                    }
                                                }
                                            }
                                        }
                                    },
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (selectedLocation?.name == location.name)
                                        MaterialTheme.colorScheme.primaryContainer
                                    else
                                        MaterialTheme.colorScheme.surfaceVariant
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
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(4.dp)
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.LocationOn,
                                                contentDescription = null,
                                                modifier = Modifier.size(16.dp),
                                                tint = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = location.name,
                                                style = MaterialTheme.typography.titleMedium,
                                                fontWeight = FontWeight.Medium,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                        Text(
                                            text = location.address,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        location.events.forEach { event ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Event,
                                                    contentDescription = null,
                                                    modifier = Modifier.size(12.dp),
                                                    tint = MaterialTheme.colorScheme.primary
                                                )
                                                Spacer(modifier = Modifier.width(4.dp))
                                                Text(
                                                    text = event,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                            }
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(20.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer
                                    ) {
                                        Text(
                                            text = "${location.eventCount} evento",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    LocationDisabledAlert(
        show = showLocationDisabledAlert,
        onAction = {
            context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        },
        onHide = { showLocationDisabledAlert = false }
    )

    PermissionDeniedAlert(
        show = showPermissionDeniedAlert,
        onAction = {
            permissionHandler.launchPermissionRequest()
        },
        onHide = { showPermissionDeniedAlert = false }
    )

    PermissionPermanentlyDeniedSnackbar(
        snackbarHostState = snackbarHostState,
        show = showPermissionPermanentlyDeniedSnackbar,
        onAction = {
            context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                data = android.net.Uri.parse("package:${context.packageName}")
            })
        },
        onHide = { showPermissionPermanentlyDeniedSnackbar = false }
    )
}

data class EventLoc(
    val name: String,
    val address: String,
    val eventCount: Int,
    val geoPoint: GeoPoint,
    val events: List<String>
)