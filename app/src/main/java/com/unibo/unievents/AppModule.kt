package com.unibo.unievents

import com.unibo.unievents.data.repositories.MapRepository
import com.unibo.unievents.data.repositories.AuthRepository
import com.unibo.unievents.data.repositories.EventRepository
import com.unibo.unievents.data.repositories.UserRepository
import com.unibo.unievents.ui.screens.addFriend.AddFriendViewModel
import com.unibo.unievents.ui.screens.board.BoardViewModel
import com.unibo.unievents.ui.screens.createEvent.CreateEventViewModel
import com.unibo.unievents.ui.screens.friends.FriendsViewModel
import com.unibo.unievents.ui.screens.homepage.HomePageViewModel
import com.unibo.unievents.ui.screens.login.LoginViewModel
import com.unibo.unievents.ui.screens.map.MapViewModel
import com.unibo.unievents.ui.screens.profile.ProfileViewModel
import com.unibo.unievents.ui.screens.registration.RegistrationViewModel
import com.unibo.unievents.ui.screens.research.ResearchViewModel
import com.unibo.unievents.utils.NetworkObserver
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.Auth
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.storage.Storage
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.http.HttpHeaders
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val AppModule = module {
    single { NetworkObserver(get()) }

    single<HttpClient> {
        HttpClient {
            defaultRequest {
                headers.append(
                    HttpHeaders.UserAgent,
                    "UniEvents/1.0 (Android)"
                )
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    single { MapRepository(get()) }

    single<SupabaseClient> {
        createSupabaseClient(
            supabaseUrl = "https://qhgaujakvtxryayfijhz.supabase.co",
            supabaseKey = "sb_publishable_IsSWVzZ0e0MB1O5eGc0-xg_ZUjDU2Q7"
        ) {
            install(Postgrest)
            install(Auth)
            install(Storage)
        }
    }

    single { AuthRepository(get()) }
    single { EventRepository(get()) }
    single { UserRepository(get()) }

    viewModel { RegistrationViewModel(get()) }
    viewModel { LoginViewModel(get()) }
    viewModel { ProfileViewModel(get()) }
    viewModel { HomePageViewModel(get(), get()) }
    viewModel { CreateEventViewModel(get(), get()) }
    viewModel { BoardViewModel(get()) }
    viewModel { ResearchViewModel(get()) }
    viewModel { MapViewModel(get()) }
    viewModel { FriendsViewModel(get()) }
    viewModel { AddFriendViewModel(get()) }
}
