package com.unibo.unievents.ui.screens.map

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.TopBar
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.CustomZoomButtonsController

@Composable
fun MapEventsScreen(
    navController: NavHostController
) {
    val context = LocalContext.current

    // Stato per la mappa e il marker selezionato
    var mapViewRef by remember { mutableStateOf<MapView?>(null) }
    var selectedLocation by remember { mutableStateOf<EventLoc?>(null) }

    // Lista eventi per località
    val eventLocations = remember {
        listOf(
            EventLoc(
                name = "Laboratorio Informatica",
                address = "Via Mura Anteo Zamboni 7 Bologna",
                eventCount = 1,
                geoPoint = GeoPoint(44.4945, 11.3420),
                events = listOf("Hackathon UniBO 2026")
            ),
            EventLoc(
                name = "Sala Feste Campus",
                address = "Via Belmeloro 14 Bologna",
                eventCount = 1,
                geoPoint = GeoPoint(44.4949, 11.3426),
                events = listOf("Festa di Carnevale Universitaria")
            ),
            EventLoc(
                name = "Aula Magna",
                address = "Piazza Verdi 2 Bologna",
                eventCount = 1,
                geoPoint = GeoPoint(44.4942, 11.3418),
                events = listOf("Conferenza Intelligenza Artificiale")
            )
        )
    }

    // Stato permessi
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        hasLocationPermission = isGranted
    }

    Scaffold(
        topBar = { TopBar(navController, "Bacheca") },
        bottomBar = { BottomBar(navController)},
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Titolo
            Text(
                text = "Mappa Eventi",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.padding(16.dp)
            )

            // Mappa OpenStreetMap
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
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

                            val cesenaCenter = GeoPoint(44.143333,12.249722 )
                            // val bolognaCenter = GeoPoint(44.4949, 11.3426)
                            controller.setZoom(13.0)
                            controller.setCenter(cesenaCenter)

                            eventLocations.forEach { location ->
                                val marker = Marker(this)
                                marker.position = location.geoPoint
                                marker.title = location.name
                                marker.subDescription = "${location.eventCount} evento - Clicca per dettagli"
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
            }

            Spacer(modifier = Modifier.height(8.dp))

            if (!hasLocationPermission) {
                Button(
                    onClick = { permissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION) },
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Abilita posizione per vedere eventi vicini")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Mappa Interattiva",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Text(
                text = "Clicca sui marker sulla mappa o sulle card qui sotto per vedere i luoghi degli eventi",
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
                                // Al click sulla card, centra la mappa sulla location
                                selectedLocation = location
                                mapViewRef?.let { mapView ->
                                    mapView.controller.animateTo(location.geoPoint)
                                    mapView.controller.setZoom(16.0)

                                    // Aggiorna il marker evidenziato
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
                                Text(
                                    text = location.name,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
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

data class EventLoc(
    val name: String,
    val address: String,
    val eventCount: Int,
    val geoPoint: GeoPoint,
    val events: List<String>
)
