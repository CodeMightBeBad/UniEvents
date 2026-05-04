package com.unibo.unievents

import com.unibo.unievents.data.repositories.AuthRepository
import com.unibo.unievents.data.repositories.EventRepository
import com.unibo.unievents.ui.screens.board.BoardViewModel
import com.unibo.unievents.ui.screens.createEvent.CreateEventViewModel
import com.unibo.unievents.ui.screens.homepage.HomePageViewModel
import com.unibo.unievents.ui.screens.login.LoginViewModel
import com.unibo.unievents.ui.screens.profile.ProfileViewModel
import com.unibo.unievents.ui.screens.registration.RegistrationViewModel
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = "https://qhgaujakvtxryayfijhz.supabase.co",
            supabaseKey = "sb_publishable_IsSWVzZ0e0MB1O5eGc0-xg_ZUjDU2Q7"
        ) {
            install(Postgrest)
            install(Auth)
        }
    }
    single { AuthRepository(get()) }
    single { EventRepository(get()) }

    viewModel { RegistrationViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { HomePageViewModel(get()) }
    viewModel { CreateEventViewModel(get()) }
    viewModel { BoardViewModel(get()) }
}
