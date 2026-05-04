package com.unibo.unievents.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.navigation.NavHostController
import com.unibo.unievents.data.repositories.AuthRepository
import com.unibo.unievents.ui.NavigationRoute
import kotlinx.coroutines.launch
import org.koin.compose.koinInject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavHostController,
    title: String,
    showMenu: Boolean = true,
    // isAdmin: Boolean = false
) {

    var menuExpanded by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val authRepository = koinInject<AuthRepository>()
    TopAppBar(
        title = { Text(title) },
        navigationIcon = {
            if (navController.previousBackStackEntry != null) {
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Go back")
                }
            }
        },
        actions = {
            if (showMenu) {
                IconButton(onClick = { menuExpanded = true }) {
                    Icon(Icons.Filled.Menu, "User menu")
                }
                DropdownMenu(
                    expanded = menuExpanded,
                    onDismissRequest = { menuExpanded = false}
                ) {
                    // if (isAdmin) {
                    //     DropdownMenuItem(
                    //         text = { Text("Admin Dashboard") },
                    //         onClick = {
                    //             menuExpanded = false
                    //             navController.navigate(NavigationRoute.AdminBoard)
                    //         }
                    //    )
                    // }
                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.Filled.Dashboard, "bacheca")
                        },
                        text = { Text("Bacheca") },
                        onClick = {
                            menuExpanded = false
                            navController.navigate(NavigationRoute.AdminBoard)
                        }
                    )

                    HorizontalDivider()

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.Filled.Search, "password")
                        },
                        text = { Text("Cerca") },
                        onClick = {
                            menuExpanded = false
                            navController.navigate(NavigationRoute.Research)
                        }
                    )

                    HorizontalDivider()

                    DropdownMenuItem(
                        leadingIcon = {
                            Icon(Icons.AutoMirrored.Filled.Logout, "logout")
                        },
                        text = { Text("Logout") },
                        onClick = {
                            menuExpanded = false
                            coroutineScope.launch {
                                authRepository.logout()
                                navController.navigate(NavigationRoute.Login) {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
}