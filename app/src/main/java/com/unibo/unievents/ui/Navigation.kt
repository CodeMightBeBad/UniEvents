package com.unibo.unievents.ui

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unibo.unievents.ui.screens.login.LoginScreen
import kotlinx.serialization.Serializable

sealed interface NavigationRoute {
    @Serializable data object Login: NavigationRoute
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Login
    ) {
        composable<NavigationRoute.Login> {
            LoginScreen(navController)
        }
    }
}
