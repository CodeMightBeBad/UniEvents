package com.unibo.unievents.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Event(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("date") val date: LocalDate,
    @SerialName("time") val time: LocalTime,
    @SerialName("address") val address: String,
    @SerialName("max_participants") val maxParticipants: Int?
)

// Specifically created to handle inserts, no ID field
@Serializable
data class EventInsert(
    @SerialName("title") val title: String,
    @SerialName("description") val description: String,
    @SerialName("date") val date: LocalDate,
    @SerialName("time") val time: LocalTime,
    @SerialName("address") val address: String,
    @SerialName("max_participants") val maxParticipants: Int?
)

@Serializable
data class User(
    @SerialName("id") val id: String,
    @SerialName("badge_number") val badgeNumber: String,
    @SerialName("profile_picture") val profilePicture: String?,
    @SerialName("score") val score: Int,
    @SerialName("email") val email: String
)
