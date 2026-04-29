package com.unibo.unievents.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("date") val date: String,
    @SerialName("time") val time: String,
    @SerialName("address") val address: String,
    @SerialName("max_participants") val maxParticipants: Int?
)