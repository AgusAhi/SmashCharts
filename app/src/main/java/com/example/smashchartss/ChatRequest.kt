package com.example.smashchartss

import kotlinx.serialization.Serializable

@Serializable
data class ChatRequest(
    val model: String,
    val prompt: String,
    val max_tokens: Int
)

