package com.unibo.unievents.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unibo.unievents.ui.screens.homepage.HomePageScreen
import com.unibo.unievents.ui.screens.homepage.HomePageViewModel
import com.unibo.unievents.ui.screens.login.LoginScreen
import com.unibo.unievents.ui.screens.login.LoginViewModel
import com.unibo.unievents.ui.screens.profile.ProfileScreen
import com.unibo.unievents.ui.screens.profile.ProfileViewModel
import com.unibo.unievents.ui.screens.registration.RegistrationScreen
import com.unibo.unievents.ui.screens.registration.RegistrationViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.status.SessionStatus
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject

sealed interface NavigationRoute {
    @Serializable data object Register : NavigationRoute
    @Serializable data object Login : NavigationRoute
    @Serializable data object Splash : NavigationRoute
    @Serializable data object Home : NavigationRoute
    @Serializable data object Profile : NavigationRoute
    @Serializable data object AddEvent : NavigationRoute
}

@Composable
fun NavGraph(navController: NavHostController) {
    val supabase = koinInject<SupabaseClient>()
    val sessionStatus by supabase.auth.sessionStatus.collectAsStateWithLifecycle()

    // Checking what is the session status
    LaunchedEffect(sessionStatus) {
        when(sessionStatus) {
            // If the user is authenticated, redirect to the homepage
            is SessionStatus.Authenticated -> {
                navController.navigate(NavigationRoute.Home) {
                    // When redirected, remove the splash screen from the navigation backstack
                    popUpTo(0) { inclusive = true }
                }
            }
            // If the user is not authenticated,redirect to the login screen
            is SessionStatus.NotAuthenticated -> {
                navController.navigate(NavigationRoute.Login) {
                    popUpTo(0) { inclusive = true }
                }
            }

            else -> { }
        }
    }

    NavHost(
        navController = navController,
        startDestination = NavigationRoute.Splash
    ) {
        // Screen used to check the authentication status at app startup
        composable<NavigationRoute.Splash> {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                CircularProgressIndicator()
            }
        }

        composable<NavigationRoute.Register> {
            val vm = koinViewModel<RegistrationViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            RegistrationScreen(state, vm.actions, navController)
        }

        composable<NavigationRoute.Login> {
            val vm = koinViewModel<LoginViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            LoginScreen(state, vm.actions, navController)
        }

        composable<NavigationRoute.Home> {
            val vm = koinViewModel<HomePageViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            HomePageScreen(state, navController)
        }

        composable<NavigationRoute.Profile> {
            val vm = koinViewModel<ProfileViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            ProfileScreen(state, vm.actions, navController)
        }
    }
}
