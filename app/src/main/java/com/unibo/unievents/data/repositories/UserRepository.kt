package com.unibo.unievents.data.repositories

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import com.unibo.unievents.data.Event
import com.unibo.unievents.data.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

@Serializable
data class EventParticipation (
    @SerialName("user_id") val userID: String,
    @SerialName("event_id") val eventID: Int
)

@Serializable
data class FriendsTable (
    @SerialName("user_id") val userID: String,
    @SerialName("friend_id") val friendID: String,
    @SerialName("pending") val pending: Boolean
)

class UserRepository(private val supabase: SupabaseClient) {
    suspend fun getCurrentUser(): User {
        supabase.auth.awaitInitialization()
        val currentUser = supabase.auth.currentUserOrNull()?.id
            ?: throw IllegalStateException("No user currently signed in")

        return supabase.from("user_information")
            .select {
                filter { eq("id", currentUser) }
            }
            .decodeSingle<User>()
    }

    suspend fun getUserByEmail(email: String): User? {
        return supabase.from("user_information").select {
            filter {
                eq("email", email)
            }
        }.decodeSingleOrNull()
    }

    suspend fun updateUserInformation(
        badgeNumber: String? = null,
        score: Int? = null,
        email: String? = null
    ) {
        val currentInfo = getCurrentUser()

        val newInfo = currentInfo.copy(
            id = currentInfo.id,
            badgeNumber = badgeNumber ?: currentInfo.badgeNumber,
            score = score ?: currentInfo.score,
            email = email ?: currentInfo.email
        )

        supabase.from("user_information").update(newInfo) {
            filter { eq("id", currentInfo.id) }
        }
    }

    suspend fun uploadProfilePicture(imageBytes: ByteArray) {
        val currentUser = getCurrentUser()
        val path = "${currentUser.id}/profile.jpg"

        // Upload the picture to the database
        supabase.storage["profile_pictures"].upload(
            path = path,
            data = imageBytes
        ) { upsert = true }

        // Update the record that points to the picture
        val picUrl = supabase.storage["profile_pictures"].publicUrl(path)

        supabase.from("user_information").update(currentUser.copy(profilePicture = picUrl)) {
            filter {
                eq("id", currentUser.id)
            }
        }
    }

    suspend fun downloadProfilePicture(): Bitmap? {
        val currentUser = getCurrentUser()
        if (currentUser.profilePicture == null) return null

        val imageBytes = supabase.storage.from("profile_pictures").downloadAuthenticated("${currentUser.id}/profile.jpg")
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }

    suspend fun joinEvent(eventID: Int) {
        val user = getCurrentUser().id

        supabase.from("participations").insert(EventParticipation(user, eventID))
    }

    suspend fun leaveEvent(eventID: Int) {
        val user = getCurrentUser().id

        supabase.from("participations").delete {
            filter {
                eq("user_id", user)
                eq("event_id", eventID)
            }
        }
    }

    suspend fun hasJoined(eventID: Int): Boolean {
        val user = getCurrentUser().id

        return supabase.from("participations").select {
            filter {
                eq("user_id", user)
                eq("event_id", eventID)
            }
        }.decodeList<EventParticipation>().isEmpty()
    }

    suspend fun getOwnEvents(): List<Event> {
        val user = getCurrentUser().id

        return supabase.from("events").select {
            filter {
                eq("created_by", user)
            }
        }.decodeList<Event>()
    }

    suspend fun getJoinedEvents(): List<Event> {
        val user = getCurrentUser().id

        return supabase.from("participations").select(
            columns = Columns.raw("*, ...events!event_id!inner(*)")
        ) {
            filter {
                eq("user_id", user)
            }
        }.decodeList<Event>()
    }

    suspend fun sendRequest(friendID: String) {
        val user = getCurrentUser().id

        supabase.from("user_friends").insert(
            FriendsTable(user, friendID, true)
        )
    }

    suspend fun getFriends(): List<User> {
        val user = getCurrentUser().id

        return supabase.from("user_friends").select(
            columns = Columns.raw("*, ...user_information!friend_id!inner(*)")
        ) {
            filter{
                eq("user_id", user)
                eq("pending", false)
            }
        }.decodeList<User>()
    }

    suspend fun getIncomingRequests(): List<User> {
        val user = getCurrentUser().id

        return supabase.from("user_friends").select(
            columns = Columns.raw("*, ...user_information!user_id!inner(*)")
        ) {
            filter {
                eq("friend_id", user)
                eq("pending", true)
            }
        }.decodeList<User>()
    }

    suspend fun getPendingRequests(): List<User> {
        val user = getCurrentUser().id

        return supabase.from("user_friends").select(
            columns = Columns.raw("*, ...user_information!friend_id!inner(*)")
        ) {
            filter {
                eq("user_id", user)
                eq("pending", true)
            }
        }.decodeList<User>()
    }

    suspend fun acceptFriendRequest(friendID: String) {
        val currentUser = getCurrentUser().id

        // Insert specular friendship record
        supabase.from("user_friends").insert(FriendsTable(currentUser, friendID, false))

        // Update the request record to reflect changes
        supabase.from("user_friends").update(FriendsTable(friendID, currentUser, false)) {
            filter {
                eq("user_id", friendID)
                eq("friend_id", currentUser)
            }
        }
    }

    suspend fun denyFriendRequest(friendID: String) {
        val currentUser = getCurrentUser().id

        supabase.from("user_friends").delete {
            filter {
                eq("user_id", friendID)
                eq("friend_id", currentUser)
            }
        }
    }

    suspend fun removeFriend(friendID: String) {
        val currentUser = getCurrentUser().id

        // Removing the current user record
        supabase.from("user_friends").delete {
            filter {
                eq("user_id", currentUser)
                eq("friend_id", friendID)
            }
        }

        // Removing the specular record for the other user
        supabase.from("user_friends").delete {
            filter {
                eq("friend_id", currentUser)
                eq("user_id", friendID)
            }
        }
    }

    suspend fun isFriend(friendID: String): Boolean {
        val currentUser = getCurrentUser().id

        return !supabase.from("user_friends").select {
            filter {
                eq("user_id", currentUser)
                eq("friend_id", friendID)
                eq("pending", false)
            }
        }.decodeList<FriendsTable>().isEmpty()
    }
}
