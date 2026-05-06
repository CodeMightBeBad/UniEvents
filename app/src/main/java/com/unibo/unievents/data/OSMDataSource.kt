package com.unibo.unievents.data

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OSMPlace(
    @SerialName("place_id")
    val id: Int,
    @SerialName("lat")
    val latitude: Double,
    @SerialName("lon")
    val longitude: Double,
    @SerialName("display_name")
    val displayName: String
)

class OSMDataSource(private val httpClient: HttpClient) {
    companion object {
        private const val BASE_URL = "https://nominatim.openstreetmap.org"
    }

    suspend fun searchPlaces(query: String): List<OSMPlace> {
        val url = "$BASE_URL/search?q=$query&format=json&limit=1"
        return httpClient.get(url).body()
    }

    // NUOVA FUNZIONE per autocompletamento indirizzi solo in Italia
    suspend fun searchAddressesInItaly(query: String, limit: Int = 5): List<OSMPlace> {
        if (query.length < 3) return emptyList() // Cerca solo se almeno 3 caratteri

        val url = "$BASE_URL/search?q=$query&format=json&limit=$limit&countrycodes=it&addressdetails=1"
        return try {
            httpClient.get(url).body()
        } catch (e: Exception) {
            emptyList()
        }
    }
}