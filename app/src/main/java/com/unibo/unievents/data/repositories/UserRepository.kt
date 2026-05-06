package com.unibo.unievents.data.repositories

import com.unibo.unievents.data.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.storage

class UserRepository(private val supabase: SupabaseClient) {
    suspend fun getCurrentUser(): User {
        val currentUser = supabase.auth.currentUserOrNull()?.id!!

        return supabase.from("user_information")
            .select {
                filter { eq("id", currentUser) }
            }
            .decodeSingle<User>()
    }

    suspend fun uploadProfile(imageBytes: ByteArray) {
        val currentUser = supabase.auth.currentUserOrNull()?.id!!
        val path = "$currentUser/profile.jpg"

        supabase.storage["profile_pictures"].upload(
            path = path,
            data = imageBytes
        ) { upsert = true }

        val pictureUrl = supabase.storage["profile_pictures"].publicUrl(path)
        updateProfilePicture(pictureUrl)
    }

    suspend fun updateUserInformation(
        badgeNumber: String? = null,
        score: Int? = null,
        email: String? = null
    ) {
        val currentInfo = getCurrentUser()

        val newInfo = User(
            id = currentInfo.id,
            badgeNumber = badgeNumber ?: currentInfo.badgeNumber,
            profilePicture = currentInfo.profilePicture,
            score = score ?: currentInfo.score,
            email = email ?: currentInfo.email
        )

        supabase.from("user_information").update(newInfo) {
            filter { eq("id", currentInfo.id) }
        }
    }

    suspend fun updateProfilePicture(profilePicture: String) {
        val currentInfo = getCurrentUser()
        val newInfo = currentInfo.copy(profilePicture = profilePicture)

        supabase.from("user_information").update(newInfo) {
            filter { eq("id", currentInfo.id) }
        }
    }
}
