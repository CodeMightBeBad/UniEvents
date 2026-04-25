package com.unibo.unievents.ui.screens.research

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.TopBar

@Composable
fun ResearchScreen(
    navController: NavHostController
) {
    Scaffold(
        topBar = {
            TopBar(navController, "Esplora eventi")
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {

                OutlinedTextField(
                    value = "",
                    onValueChange = {},
                    placeholder = { Text("Cerca eventi per titolo, luogo o città.") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "23 eventi trovati",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            item {
                EventCard(
                    city = "Bologna",
                    title = "Hackathon UniBO 2026",
                    location = "Laboratorio Informatica",
                    address = "Via Mura Anteo Zamboni 7",
                    date = "martedì 10 marzo - 09:00",
                    description = "Maratona di programmazione 24 ore con premi per i migliori progetti",
                    participants = "3 / 60 partecipanti"
                )
            }

            item {
                EventCard(
                    city = "Bologna",
                    title = "Festa di Carnevale Universitaria",
                    location = "Sala Feste Campus",
                    address = "Via Belmeloro 14",
                    date = "venerdì 20 febbraio - 21:00",
                    description = "Serata in maschera con DJ set e buffet",
                    participants = "2 / 150 partecipanti"
                )
            }

            item {
                EventCard(
                    city = "Bologna",
                    title = "Conferenza Intelligenza Artificiale",
                    location = "Aula Magna",
                    address = "Piazza Verdi 2",
                    date = "lunedì 15 marzo - 15:00",
                    description = "Conferenza sulle ultime scoperte nel campo dell'IA",
                    participants = "45 / 200 partecipanti"
                )
            }
        }
    }
}

@Composable
fun EventCard(
    city: String,
    title: String,
    location: String,
    address: String,
    date: String,
    description: String,
    participants: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = city,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )

            Text(
                text = location,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = address,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = date,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Surface(
                shape = RoundedCornerShape(12.dp),
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = participants,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}