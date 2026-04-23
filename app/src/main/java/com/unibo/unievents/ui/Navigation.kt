package com.unibo.unievents.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unibo.unievents.ui.screens.registration.RegistrationScreen
import com.unibo.unievents.ui.screens.registration.RegistrationViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel

sealed interface NavigationRoute {
    @Serializable data object Register : NavigationRoute
}

@Composable
fun NavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Register
    ) {
        composable<NavigationRoute.Register> {
            val vm = koinViewModel<RegistrationViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            RegistrationScreen(state, vm.actions, navController)
        }
    }
}
