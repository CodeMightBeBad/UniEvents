package com.unibo.unievents.data.repositories

import com.unibo.unievents.data.Event
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.postgrest.from

class EventRepository(private val supabase: SupabaseClient) {
    suspend fun getEvents(): List<Event> {
        return supabase.from("events")
            .select()
            .decodeList<Event>()
    }

    suspend fun createEvent(event: Event): Result<Unit> {
        return try {
            supabase.from("events").insert(event)

            Result.success(Unit)
        } catch (_: Exception) {
            Result.failure(Exception("Error during event save"))
        }
    }
}
