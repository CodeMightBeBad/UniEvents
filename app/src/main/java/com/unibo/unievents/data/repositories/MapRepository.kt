package com.unibo.unievents.data.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.headers
import io.ktor.http.HttpHeaders
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapResult(
    @SerialName("lat") val latitude: String,
    @SerialName("lon") val longitude: String,
    @SerialName("display_name") val name: String
)

class MapRepository(private val httpClient: HttpClient) {
    companion object {
        private const val BASE_URL = "https://nominatim.openstreetmap.org"
    }

    suspend fun addressLookup(address: String): List<MapResult> {
        val requestUrl = "$BASE_URL/search?q=${address.replace(' ', '+')}&format=jsonv2&limit=5&countrycodes=it&accept-language=it-it"

        val response = httpClient.get(requestUrl) {
            headers{
                append(HttpHeaders.Accept, "application/json")
                append(HttpHeaders.UserAgent, "Ktor client, android application")
            }
        }

        return response.body()
    }
}