package com.unibo.unievents.ui

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.unibo.unievents.ui.screens.addFriend.AddFriendScreen
import com.unibo.unievents.ui.screens.addFriend.AddFriendViewModel
import com.unibo.unievents.ui.screens.board.BoardScreen
import com.unibo.unievents.ui.screens.board.BoardViewModel
import com.unibo.unievents.ui.screens.createEvent.CreateEventScreen
import com.unibo.unievents.ui.screens.createEvent.CreateEventViewModel
import com.unibo.unievents.ui.screens.friends.FriendsScreen
import com.unibo.unievents.ui.screens.friends.FriendsViewModel
import com.unibo.unievents.ui.screens.homepage.HomePageScreen
import com.unibo.unievents.ui.screens.homepage.HomePageViewModel
import com.unibo.unievents.ui.screens.login.LoginScreen
import com.unibo.unievents.ui.screens.login.LoginViewModel
import com.unibo.unievents.ui.screens.map.MapEventsScreen
import com.unibo.unievents.ui.screens.map.MapViewModel
import com.unibo.unievents.ui.screens.myEvents.MyEventsScreen
import com.unibo.unievents.ui.screens.myEvents.MyEventsViewModel
import com.unibo.unievents.ui.screens.profile.ProfileScreen
import com.unibo.unievents.ui.screens.profile.ProfileViewModel
import com.unibo.unievents.ui.screens.registration.RegistrationScreen
import com.unibo.unievents.ui.screens.registration.RegistrationViewModel
import com.unibo.unievents.ui.screens.research.ResearchScreen
import com.unibo.unievents.ui.screens.research.ResearchViewModel
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
    @Serializable data object AdminBoard : NavigationRoute
    @Serializable data object Research : NavigationRoute
    @Serializable data object Map : NavigationRoute
    @Serializable data object Friends : NavigationRoute
    @Serializable data object AddFriend : NavigationRoute
    @Serializable data object MyEvents : NavigationRoute
}

@Composable
fun NavGraph(navController: NavHostController) {
    val supabase = koinInject<SupabaseClient>()
    val sessionStatus by supabase.auth.sessionStatus.collectAsStateWithLifecycle()

    // Checking what is the session status
    LaunchedEffect(sessionStatus) {
        Log.d("Navigation", sessionStatus.toString())

        when(sessionStatus) {
            // If the user is authenticated either redirect to homepage or do nothing
            is SessionStatus.Authenticated -> {
                val currentPage = navController.currentBackStackEntry?.destination

                if (
                    currentPage?.hasRoute<NavigationRoute.Splash>() == true ||
                    currentPage?.hasRoute<NavigationRoute.Login>() == true ||
                    currentPage?.hasRoute<NavigationRoute.Register>() == true
                ) {
                    navController.navigate(NavigationRoute.Home) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            }

            // If the user is not authenticated,redirect to the login screen
            is SessionStatus.NotAuthenticated -> {
                navController.navigate(NavigationRoute.Login) {
                    popUpTo(0) { inclusive = true }
                }
            }

            // If the session refresh fails, clear the session data
            is SessionStatus.RefreshFailure -> {
                supabase.auth.clearSession()
            }

            // If the session is still initializing, do nothing
            is SessionStatus.Initializing -> { }
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

            HomePageScreen(state, vm.actions, navController)
        }

        composable<NavigationRoute.Profile> {
            val vm = koinViewModel<ProfileViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            ProfileScreen(state, vm.actions, navController)
        }

        composable<NavigationRoute.AddEvent> {
            val vm = koinViewModel<CreateEventViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            CreateEventScreen(state, vm.actions, navController)
        }

        composable<NavigationRoute.AdminBoard> {
            val vm = koinViewModel<BoardViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            BoardScreen(state, vm.actions, navController)
        }

        composable<NavigationRoute.Research> {
            val vm = koinViewModel<ResearchViewModel>()

            ResearchScreen(vm, navController)
        }

        composable<NavigationRoute.Map> {
            val vm = koinViewModel<MapViewModel>()

            MapEventsScreen(navController, vm)
        }

        composable<NavigationRoute.Friends> {
            val vm = koinViewModel<FriendsViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            FriendsScreen(state, vm.actions, navController)
        }

        composable<NavigationRoute.AddFriend> {
            val vm = koinViewModel<AddFriendViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            AddFriendScreen(state, vm.actions, navController)
        }

        composable<NavigationRoute.MyEvents> {
            val vm = koinViewModel<MyEventsViewModel>()
            val state by vm.state.collectAsStateWithLifecycle()

            MyEventsScreen(state, vm.actions, navController)
        }
    }
}
