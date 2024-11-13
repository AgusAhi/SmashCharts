@file:OptIn(SupabaseExperimental::class)

package com.example.smashchartss

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import io.github.jan.supabase.annotations.SupabaseExperimental

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CharacterList(onCharacterClick = { characterId ->})
        }
    }
}


/*
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CharacterList(onCharacterClick = {})
        }
    }
}

fun SupaBase () {
    @OptIn(SupabaseExperimental::class, SupabaseInternal::class)
    val supabaseClient = createSupabaseClient(
        supabaseUrl = "https://jizdtwofjbzobjipqnha.supabase.co",
        supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImppemR0d29mamJ6b2JqaXBxbmhhIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MzEzMzU5NjksImV4cCI6MjA0NjkxMTk2OX0.yyhbbUXCq0hQCvGLaxgIe0YOjgEuiGiL_jNMAeVZyoQ"
    ) {
        install(Realtime)
        install(Postgrest)
        httpConfig {
            install(WebSockets)
        }
    }
}

 */



