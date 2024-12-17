package com.example.smashchartss.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Series(
    val id: String,
    val name: String,
    val icon_url: String
)
