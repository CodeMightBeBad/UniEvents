package com.unibo.unievents.ui.screens.homepage

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.TopBar

@Composable
fun HomePageScreen(
    navController: NavHostController,
    viewModel: HomePageViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    // Scaffold con la tua TopBar e BottomBar già esistenti
    Scaffold(
        topBar = {
            TopBar(navController, "homepage")
        },
        bottomBar = {
            BottomBar(navController)
        },
        floatingActionButton = {
            // PULSANTE + IN BASSO A DESTRA
            FloatingActionButton(
                onClick = {
                    // Azione quando premi il +
                    // viewModel.onCreateEventClick()
                },
                containerColor = Color(0xFF6200EE),
                contentColor = Color.White,
                shape = CircleShape,
                modifier = Modifier.size(56.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Crea evento",
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End, // Fine = destra, Start = sinistra
        // isFloatingActionButtonDocked = false // Se true si attacca alla bottom bar
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is HomeUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is HomeUiState.Success -> {
                    val groupedEvents = (uiState as HomeUiState.Success).eventsGroupedByDate
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFF5F5F5)),
                        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(24.dp)
                    ) {
                        items(groupedEvents) { eventsByDate ->
                            Column(
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Data header
                                Text(
                                    text = viewModel.formatDate(eventsByDate.date),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF6200EE),
                                    modifier = Modifier.padding(start = 8.dp)
                                )

                                // Event cards for this date
                                eventsByDate.events.forEach { event ->
                                    EventCard(
                                        event = event,
                                        onParticipateClick = { viewModel.onParticipateClick(event) },
                                        onMoreInfoClick = { viewModel.onMoreInfoClick(event) },
                                        formatTime = { viewModel.formatTime(it) },
                                        getParticipantText = { current, max ->
                                            viewModel.getParticipantText(current, max)
                                        }
                                    )
                                }
                            }
                        }
                    }
                }

                is HomeUiState.Error -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = (uiState as HomeUiState.Error).message,
                                color = Color.Red
                            )
                            Button(
                                onClick = { /* Ricarica */ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF6200EE)
                                )
                            ) {
                                Text("Riprova")
                            }
                        }
                    }
                }
            }
        }
    }
}

// Mantieni la tua EventCard invariata
@Composable
fun EventCard(
    event: Event,
    onParticipateClick: () -> Unit,
    onMoreInfoClick: () -> Unit,
    formatTime: (java.time.LocalTime) -> String,
    getParticipantText: (Int, Int) -> String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = event.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF333333)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            modifier = Modifier.size(16.dp),
                            tint = Color(0xFF757575)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = event.location,
                            fontSize = 14.sp,
                            color = Color(0xFF757575)
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(20.dp),
                    color = Color(0xFFE3F2FD),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text(
                        text = getParticipantText(event.currentParticipants, event.maxParticipants),
                        fontSize = 12.sp,
                        color = Color(0xFF1976D2),
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Business,
                    contentDescription = "Venue",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF9E9E9E)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = event.venue,
                    fontSize = 13.sp,
                    color = Color(0xFF9E9E9E)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Schedule,
                    contentDescription = "Time",
                    modifier = Modifier.size(16.dp),
                    tint = Color(0xFF9E9E9E)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = formatTime(event.time),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color(0xFF333333)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = event.description,
                fontSize = 14.sp,
                color = Color(0xFF666666),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = onMoreInfoClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color(0xFF6200EE)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "More Info",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("MAGGIORI INFO")
                }

                Button(
                    onClick = onParticipateClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF6200EE)
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Participate",
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("PARTECIPA")
                }
            }
        }
    }
}