package com.unibo.unievents.ui.composables

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.unibo.unievents.ui.NavigationRoute

@Composable
fun BottomBar(navController: NavHostController) {
    BottomAppBar(
        actions = {
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                IconButton(
                    onClick = {
                        navController.navigate(NavigationRoute.Home) {
                            popUpTo(0)
                        }
                    }
                ) {
                    Icon(Icons.Filled.Home, "Home")
                }

                IconButton(
                    onClick = {
                        navController.navigate(NavigationRoute.Map) {
                            popUpTo(0)
                        }
                    }
                ) {
                    Icon(Icons.Filled.LocationOn, "Map")
                }

                IconButton(onClick = {
                    navController.navigate(NavigationRoute.MyEvents) {
                        popUpTo(0)
                    }
                }) {
                    Icon(Icons.Filled.DateRange, "Calendar")
                }

                IconButton(
                    onClick = {
                        navController.navigate(NavigationRoute.Profile) {
                            popUpTo(0)
                        }
                    }
                ) {
                    Icon(Icons.Filled.Person, "User")
                }
            }
        }
    )
}
