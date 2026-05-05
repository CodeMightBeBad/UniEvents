package com.unibo.unievents.data.repositories

import android.util.Log
import com.unibo.unievents.data.User
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.postgrest.from

class UserRepository(private val supabase: SupabaseClient) {
    suspend fun getCurrentUser(): User {
        val currentUser = supabase.auth.currentUserOrNull()?.id!!

        return supabase.from("user_information")
            .select {
                filter { eq("id", currentUser) }
            }
            .decodeSingle<User>()
    }
}