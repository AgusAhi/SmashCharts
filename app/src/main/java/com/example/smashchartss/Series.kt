package com.example.smashchartss

import kotlinx.serialization.Serializable

@Serializable
data class Series(
    val id: String,
    val name: String,
    val icon_url: String
)
