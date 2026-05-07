package com.unibo.unievents.ui.screens.homepage

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.NavigationRoute
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.EventCard
import com.unibo.unievents.ui.composables.TopBar

@Composable
fun HomePageScreen(
    state: HomePageState,
    navController: NavHostController
) {
    if (state.loading) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    } else {
        Scaffold(
            topBar = { TopBar(navController, "Homepage") },
            bottomBar = { BottomBar(navController) },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { navController.navigate(NavigationRoute.AddEvent) },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onSurface,
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
            floatingActionButtonPosition = FabPosition.End,
            containerColor = MaterialTheme.colorScheme.background
        ) { paddingValues ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(state.events) {
                    EventCard(it)
                }
            }
        }
    }
}
