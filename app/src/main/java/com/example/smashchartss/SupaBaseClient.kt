package com.example.smashchartss

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.jan.supabase.annotations.SupabaseExperimental
import io.github.jan.supabase.annotations.SupabaseInternal
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.postgrest
import io.github.jan.supabase.realtime.Realtime
import io.ktor.client.plugins.websocket.WebSockets
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import android.util.Log

// Cliente Supabase
object SupaBaseClient {
    @OptIn(SupabaseExperimental::class, SupabaseInternal::class)
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

// Función para obtener la lista de personajes desde Supabase
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

// Repositorio
class CharacterRepository {
    private suspend fun fetchCharacterList(): Result<List<Character>> {
        return try {
            val characters = fetchCharacters()
            Result.success(characters)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun getCharactersFlow(): Flow<Result<List<Character>>> = flow {
        emit(fetchCharacterList())
    }

    suspend fun getCharacterById(id: Int): Result<Character> {
        return try {
            val characters = fetchCharacters()
            val character = characters.find { it.number == id }
                ?: throw NoSuchElementException("Character with id $id not found")
            Result.success(character)
        } catch (e: Exception) {
            Log.e("Supabase", "Error fetching character by ID: ${e.message}", e)
            Result.failure(e)
        }
    }
}

// Estados para la UI
sealed class CharacterListUiState {
    object Loading : CharacterListUiState()
    data class Success(val characters: List<Character>) : CharacterListUiState()
    data class Error(val message: String) : CharacterListUiState()
}

sealed class CharacterDetailsUiState {
    object Loading : CharacterDetailsUiState()
    data class Success(val character: Character) : CharacterDetailsUiState()
    data class Error(val message: String) : CharacterDetailsUiState()
}

class CharacterViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _characters = MutableStateFlow<CharacterListUiState>(CharacterListUiState.Loading)
    val characters: StateFlow<CharacterListUiState> = _characters.asStateFlow()

    init {
        loadCharacters()
    }

    private fun loadCharacters() {
        viewModelScope.launch {
            repository.getCharactersFlow()
                .catch { e ->
                    _characters.value = CharacterListUiState.Error(e.message ?: "Error loading characters")
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { characterList ->
                            _characters.value = CharacterListUiState.Success(characterList)
                        },
                        onFailure = {
                            _characters.value = CharacterListUiState.Error(it.message ?: "Error loading characters")
                        }
                    )
                }
        }
    }
}

