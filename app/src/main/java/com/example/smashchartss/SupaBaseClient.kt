package com.example.smashchartss

import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.plugins.websocket.WebSockets
import io.github.jan.supabase.postgrest.Postgrest.Companion as Postgrest1

object SupaBaseClient {
    @OptIn(SupabaseExperimental::class, SupabaseInternal::class, SupabaseInternal::class)
    val client = createSupabaseClient(
        supabaseUrl = "https://jizdtwofjbzobjipqnha.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImppemR0d29mamJ6b2JqaXBxbmhhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzEzMzU5NjksImV4cCI6MjA0NjkxMTk2OX0.yyhbbUXCq0hQCvGLaxgIe0YOjgEuiGiL_jNMAeVZyoQ"
    ) {
        install(Realtime)
        install(Postgrest1)
        httpConfig {
            install(WebSockets)
        }
    }
}

suspend fun fetchCharacters(): List<Character> {
    val result = SupaBaseClient.client
        .postgrest
        .from("characters")
        .select()
        .decodeList<Character>()
        .first()

    return listOf(result)
}

