package com.example.smashchartss

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewModelScope

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log
import io.github.jan.supabase.annotiations.SupabaseInternal
import io.github.jan.supabase.postgrest.rpc
import kotlinx.serialization.Serializable





// CONEXION A SUPABASE
object SupaBaseClient {
    @OptIn(SupabaseInternal::class)
    val client = createSupabaseClient(
        supabaseUrl = "https://jizdtwofjbzobjipqnha.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImppemR0d29mamJ6b2JqaXBxbmhhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzEzMzU5NjksImV4cCI6MjA0NjkxMTk2OX0.yyhbbUXCq0hQCvGLaxgIe0YOjgEuiGiL_jNMAeVZyoQ"
    ) {
        install(Realtime)
        install(io.github.jan.supabase.postgrest.Postgrest)
        httpConfig {
            install(WebSockets)
        }
    }
}




// TABLA LISTA DE PERSONAJES
suspend fun fetchCharacters(): List<Character> {
    return try {
        SupaBaseClient.client
            .postgrest
            .from("characters")
            .select()
            .decodeList()
    } catch (e: Exception) {
        Log.e("Supabase", "Error fetching characters: ${e.message}", e)
        emptyList() // En caso de error, devuelve una lista vacía
    }
}

suspend fun fetchChangesByCharacterId(characterId: String): List<Changes> {
    return try {
        SupaBaseClient.client
            .postgrest
            .rpc("fetch_changes_by_character_id", mapOf("character_id" to characterId))
            .decodeList<Changes>()
    } catch (e: Exception) {
        Log.e("Supabase", "Error fetching changes: ${e.message}", e)
        emptyList() // En caso de error, devuelve una lista vacía
    }
}












