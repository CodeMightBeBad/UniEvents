package com.unibo.unievents.data.repositories

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.exceptions.RestException
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject

@Serializable
data class RegistrationMetadata(
    @SerialName("badge_number") val badgeNumber: String,
    @SerialName("username") val username: String
)

class AuthRepository(private val supabase: SupabaseClient) {
    suspend fun register(email: String, password: String, badgeNumber: String, username: String) {
        supabase.auth.signUpWith(Email) {
            this.email = email
            this.password = password

            this.data = Json.encodeToJsonElement(
                RegistrationMetadata(
                    badgeNumber = badgeNumber,
                    username = username
                )
            ).jsonObject
        }
    }

    suspend fun login(email: String, password: String): Result<Unit> {
        return try {
            supabase.auth.signInWith(Email) {
                this.email = email
                this.password = password
            }

            Result.success(Unit)
        } catch (_: RestException) {
            Result.failure(Exception("Invalid email or password"))
        } catch (_: Exception) {
            Result.failure(Exception("Error during login"))
        }
    }

    suspend fun logout() {
        supabase.auth.signOut()
    }
}
