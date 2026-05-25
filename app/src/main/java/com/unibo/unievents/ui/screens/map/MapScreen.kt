package com.unibo.unievents.ui.screens.map

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.preference.PreferenceManager
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.TopBar
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.CustomZoomButtonsController
import org.osmdroid.views.MapView

@Composable
fun MapScreen(
    state: MapState,
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
                    EventsMap()
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text (
                text = "Eventi:",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )

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
        }
    }
}

@Composable
fun EventsMap() {
    val ctx = LocalContext.current

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
                controller.setCenter(GeoPoint(44.0633, 12.5707))
            }
        },
        update = { mapView ->
            // TODO: handle the points updating (?)
        }
    )
}