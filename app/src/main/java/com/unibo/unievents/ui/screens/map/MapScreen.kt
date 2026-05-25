package com.unibo.unievents.ui.screens.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.navigation.NavHostController
import androidx.preference.PreferenceManager
import com.unibo.unievents.data.Event
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.TopBar
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun MapScreen(
    state: MapState,
    actions: MapActions,
    navController: NavHostController
) {
    Scaffold(
        topBar = { TopBar(navController, "Mappa eventi") },
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(15.dp)
        ) {
            Card (
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
            ) {
                Box {
                    EventsMap(state.events, state.selectedEvent)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text (
                text = "Eventi:",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (state.events.isEmpty()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Non ci sono eventi in programma",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(state.events) { event ->
                        EventCard(
                            title = event.title,
                            address = event.address,
                            onClick = {
                                actions.selectEvent(event.latitude.toDouble(), event.longitude.toDouble())
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun EventsMap(
    events: List<Event>,
    selectedEvent: Pair<Double, Double>?
) {
    val ctx = LocalContext.current
    var lastSelection by remember { mutableStateOf<Pair<Double, Double>?>(null) }

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

                if (selectedEvent == null) {
                    controller.setCenter(GeoPoint(44.0633, 12.5707))
                } else {
                    controller.setCenter(GeoPoint(selectedEvent.first, selectedEvent.second))
                    lastSelection = selectedEvent
                }
            }
        },
        update = { mapView ->
            mapView.overlays.removeAll { it is Marker }

            events.forEach { event ->
                val marker = Marker(mapView).apply {
                    position = GeoPoint(event.latitude.toDouble(), event.longitude.toDouble())
                }

                mapView.overlays.add(marker)
            }

            if (selectedEvent != null && selectedEvent != lastSelection) {
                mapView.controller.animateTo(GeoPoint(selectedEvent.first, selectedEvent.second))
                lastSelection = selectedEvent
            }

            mapView.invalidate()
        }
    )
}

@Composable
fun EventCard(
    title: String,
    address: String,
    onClick: () -> Unit
) {
    Card (
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = address,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
