package com.unibo.unievents.data.repositories

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapResult(
    @SerialName("lat") val latitude: String,
    @SerialName("lon") val longitude: String,
    @SerialName("display_name") val name: String,
    @SerialName("place_rank") val rank: Int,
    @SerialName("address") val addressInfo: AddressComponent? = null
) {
    fun formatAddress(): String {
        if (addressInfo == null) return "Indirizzo sconosciuto"

        val parts = listOfNotNull(
            addressInfo.road,
            addressInfo.houseNumber,
            addressInfo.postcode,
            addressInfo.resolvedCity
        )

        return parts.joinToString(", ")
    }
}

@Serializable
data class AddressComponent(
    @SerialName("road") val road: String? = null,
    @SerialName("house_number") val houseNumber: String? = null,
    @SerialName("postcode") val postcode: String? = null,
    @SerialName("city") val city: String? = null,
    @SerialName("town") val town: String? = null,
    @SerialName("village") val village: String? = null
) {
    val resolvedCity: String?
        get() = city ?: town ?: village
}

class MapRepository(private val httpClient: HttpClient) {
    companion object {
        private const val BASE_URL = "https://nominatim.openstreetmap.org"
    }

    suspend fun addressLookup(address: String): List<MapResult> {
        val response = httpClient.get("$BASE_URL/search") {
            url {
                parameters.append("q", address)
                parameters.append("format", "jsonv2")
                parameters.append("limit", "10")
                parameters.append("countrycodes", "it")
                parameters.append("accept-language", "it-it")
                parameters.append("layer", "address,poi")
                parameters.append("addressdetails", "1")
            }

            headers {
                append(HttpHeaders.Accept, "application/json")
            }
        }

        return response.body<List<MapResult>>().filter { it.rank >= 28 }
    }
}