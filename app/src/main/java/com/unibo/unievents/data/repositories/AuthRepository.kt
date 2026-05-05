package com.unibo.unievents.data.repositories

import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.postgrest
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

@Serializable
data class Admin(
    @SerialName("id") val id: String
)

class AuthRepository(private val supabase: SupabaseClient) {
    suspend fun register(email: String, password: String, badgeNumber: String, username: String): Result<Unit> {
        return try {
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

            Result.success(Unit)
        } catch (_: Exception) {
            Result.failure(Exception("Error during registration"))
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

    suspend fun isAdmin(): Boolean {
        val currentUser = supabase.auth.currentUserOrNull()?.id ?: return false

        val admins = supabase.postgrest["admin_users"].select {
            filter { eq("id", currentUser) }
        }.decodeList<Admin>()

        return admins.isNotEmpty()
    }
}
