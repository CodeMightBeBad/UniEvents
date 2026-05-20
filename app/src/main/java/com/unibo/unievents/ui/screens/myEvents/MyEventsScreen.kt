package com.unibo.unievents.ui.screens.myEvents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.composables.BottomBar
import com.unibo.unievents.ui.composables.CustomEventDropdown
import com.unibo.unievents.ui.composables.TopBar

@Composable
fun MyEventsScreen(
    state: MyEventsState,
    actions: MyEventsActions,
    navController: NavHostController
) {
    Scaffold(
        topBar = { TopBar(navController, "I miei eventi") },
        bottomBar = { BottomBar(navController) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomEventDropdown(
                label = "Eventi a cui partecipo",
                count = state.joinedEvents.size,
                events = state.joinedEvents,
                isExpanded = state.isJoinedExpanded,
                onExpandedChange = actions.toggleJoinedExpanded,
                onEventSelected = { }
            )

            CustomEventDropdown(
                label = "Eventi che ho creato",
                count = state.createdEvents.size,
                events = state.createdEvents,
                isExpanded = state.isCreatedExpanded,
                onExpandedChange = actions.toggleCreatedExpanded,
                onEventSelected = { }
            )
        }
    }
}