package com.unibo.unievents.data.repositories

import com.unibo.unievents.data.Event
import com.unibo.unievents.data.EventInsert
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class EventId (
    @SerialName("id") val id: Int
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
        } catch (_: Exception) {
            Result.failure(Exception("Error during event save"))
        }
    }
}
