package com.unibo.unievents

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.unibo.unievents.ui.screens.creaEvento.CreaEventoScreen
import com.unibo.unievents.ui.screens.creaEvento.CreaEventoViewModel
import com.unibo.unievents.ui.screens.mappa.MappaScreen
import com.unibo.unievents.ui.theme.UniEventsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            UniEventsTheme{
                val viewModel: CreaEventoViewModel = viewModel()
                CreaEventoScreen (viewModel, onEventCreated = {}, onCancel = {} )
            }

        }
    }
}