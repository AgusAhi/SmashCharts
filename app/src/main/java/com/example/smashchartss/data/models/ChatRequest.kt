package com.example.smashchartss.data.models

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int
)

