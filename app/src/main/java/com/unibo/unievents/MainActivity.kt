package com.unibo.unievents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.compose.rememberNavController
import com.unibo.unievents.ui.screens.registration.RegistrationScreen
import com.unibo.unievents.ui.screens.registration.RegistrationViewModel
import com.unibo.unievents.ui.theme.UniEventsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniEventsTheme {
                val navController = rememberNavController()
                val viewModel = remember { RegistrationViewModel() }

                RegistrationScreen(
                    viewModel = viewModel,
                    navController = navController,
                    onRegistrationSuccess = {
                        /*TODO*/
                    }
                )
            }
        }
    }
}