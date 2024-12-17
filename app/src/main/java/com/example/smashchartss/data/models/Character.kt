package com.example.smashchartss.data.models

import kotlinx.serialization.Serializable

@Serializable
data class Character(
    val id: String,
    val number: Int,
    val name: String,
    val icon_url: String,
    val series: String
)

