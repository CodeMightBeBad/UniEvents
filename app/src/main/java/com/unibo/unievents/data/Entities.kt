package com.unibo.unievents.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("date") val date: LocalDate,
    @SerialName("time") val time: LocalTime,
    @SerialName("address") val address: String,
    @SerialName("max_participants") val maxParticipants: Int?
)