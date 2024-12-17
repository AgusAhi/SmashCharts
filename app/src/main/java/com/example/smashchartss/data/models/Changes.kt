package com.example.smashchartss.data.models

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName

@Serializable
data class Changes(
    val id: Int,
    @SerialName("character_id") val characterId: String, // Mapea la clave JSON a esta propiedad
    val text: String
)

