package com.unibo.unievents

import com.unibo.unievents.ui.screens.registration.RegistrationViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    viewModel { RegistrationViewModel() }
}
