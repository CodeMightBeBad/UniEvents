package com.unibo.unievents.data.repositories

import com.unibo.unievents.data.Event
import com.unibo.unievents.data.EventInsert
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.exceptions.HttpRequestException
import io.github.jan.supabase.exceptions.RestException
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class EventId (
    @SerialName("id") val id: Int
)

@Serializable
data class EventParticipants (
    @SerialName("user_id") val userID: String,
    @SerialName("event_id") val eventID: Int
)

class EventRepository(private val supabase: SupabaseClient) {
    suspend fun getApprovedEvents(): List<Event> {
        return supabase.postgrest["events"]
            .select(columns = Columns.raw("*, approved_events!inner(id)"))
            .decodeList<Event>()
    }

    suspend fun getPendingEvents(): List<Event> {
        return supabase.from("pending_events")
            .select()
            .decodeList<Event>()
    }

    suspend fun deleteEvent(event: Event) {
        supabase.postgrest["events"]
            .delete { filter { eq("id", event.id) } }
    }

    suspend fun approveEvent(event: Event) {
        supabase.from("approved_events")
            .insert(EventId(event.id))
    }

    suspend fun createEvent(event: EventInsert): Result<Unit> {
        return try {
            supabase.from("events").insert(event)

            Result.success(Unit)
        } catch (_: RestException) {
            Result.failure(Exception("Database error"))
        } catch (_: HttpRequestException) {
            Result.failure(Exception("Network error"))
        }
    }

    suspend fun getPeopleCount(event: Event): Int {
        return supabase.from("participations").select {
            filter {
                eq("event_id", event.id)
            }
        }.decodeList<EventParticipation>().count()
    }

    private suspend fun uploadPhoto(photoBytes: ByteArray): String {
        val fileName = "${UUID.randomUUID()}.jpg"
        supabase.storage.from("event-photos").upload(fileName, photoBytes)
        return supabase.storage.from("event-photos").publicUrl(fileName)
    }

    suspend fun createEvent(event: EventInsert, photos: List<ByteArray>): Result<Unit> {
        return try {
            val photoUrls = photos.map { uploadPhoto(it) }
            supabase.from("events").insert(event.copy(photos = photoUrls))
            Result.success(Unit)
        } catch (e: RestException) {
        android.util.Log.e("CreateEvent", "RestException: ${e.message} - description: ${e.description} - statusCode: ${e.statusCode}")
        Result.failure(Exception("Database error: ${e.message}"))
        } catch (_: HttpRequestException) {
            Result.failure(Exception("Network error"))
        }
    }
}
