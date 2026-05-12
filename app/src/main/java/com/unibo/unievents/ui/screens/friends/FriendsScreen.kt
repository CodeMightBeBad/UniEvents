package com.unibo.unievents.ui.screens.friends

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.unibo.unievents.data.User
import com.unibo.unievents.ui.NavigationRoute
import com.unibo.unievents.ui.composables.TopBar

@Composable
fun FriendsScreen(
    state: FriendsState,
    actions: FriendsActions,
    navController: NavHostController
) {
    Scaffold(
        topBar = { TopBar(navController, "Friends") },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(NavigationRoute.AddFriend) }) {
                Icon(Icons.Filled.Add, "Add friend")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            if (state.incomingRequests.count() != 0) {
                Text(
                    text = "Richieste:",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(4.dp)
                )

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(state.incomingRequests) { user ->
                        FriendRequestCard(
                            user = user,
                            onAccept = { actions.acceptRequest(user) },
                            onDeny = { actions.denyRequest(user) }
                        )
                    }
                }
            }

            Text(
                text = "Amici:",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(4.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (state.loading) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            } else {
                if (state.friends.count() == 0) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth()
                    )
                    {
                        Text(
                            text = "Non hai ancora aggiunto nessun amico",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(state.friends) { user ->
                            FriendCard(
                                user = user,
                                onRemove = { actions.removeFriend(user) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FriendRequestCard(
    user: User,
    onAccept: () -> Unit,
    onDeny: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row (
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Person, "User placeholder")
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(user.email)

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onDeny) {
                Icon(Icons.Filled.Close, "Deny request")
            }

            IconButton(onClick = onAccept) {
                Icon(Icons.Filled.Check, "Accept request")
            }
        }
    }
}

@Composable
fun FriendCard(
    user: User,
    onRemove: () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(12.dp)
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.primaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Filled.Person, "User placeholder")
                }
            }

            Spacer(modifier = Modifier.width(10.dp))

            Text(user.email)

            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = onRemove) {
                Icon(Icons.Filled.Remove, "Remove friend")
            }
        }
    }
}
