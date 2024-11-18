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
    return SupaBaseClient.client
        .postgrest
        .from("characters")
        .select()
        .decodeList()
}

// Clases de estado para la UI
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

// Repositorio para manejar la lógica de negocio
class CharacterRepository {
    fun getCharactersFlow(): Flow<Result<List<Character>>> = flow {
        try {
            val characters = fetchCharacters()
            emit(Result.success(characters))
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    suspend fun getCharacterById(number: Int): Result<Character> {
        return try {
            val characters = fetchCharacters()
            val character = characters.find { it.number == number }
                ?: throw NoSuchElementException("Character with id $number not found")
            Result.success(character)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

// ViewModel para manejar los estados de la UI
class CharacterViewModel(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<CharacterListUiState>(CharacterListUiState.Loading)
    val uiState: StateFlow<CharacterListUiState> = _uiState.asStateFlow()

    private val _selectedCharacter = MutableStateFlow<CharacterDetailsUiState>(CharacterDetailsUiState.Loading)
    val selectedCharacter: StateFlow<CharacterDetailsUiState> = _selectedCharacter.asStateFlow()

    init {
        loadCharacters()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            _uiState.value = CharacterListUiState.Loading
            repository.getCharactersFlow()
                .catch { e ->
                    _uiState.value = CharacterListUiState.Error(e.message ?: "Unknown error")
                }
                .collect { result ->
                    result.fold(
                        onSuccess = { characters ->
                            _uiState.value = CharacterListUiState.Success(characters)
                        },
                        onFailure = { error ->
                            _uiState.value = CharacterListUiState.Error(error.message ?: "Unknown error")
                        }
                    )
                }
        }
    }

    fun loadCharacterDetails(id: Int) {
        viewModelScope.launch {
            _selectedCharacter.value = CharacterDetailsUiState.Loading
            repository.getCharacterById(id)
                .onSuccess { character ->
                    _selectedCharacter.value = CharacterDetailsUiState.Success(character)
                }
                .onFailure { error ->
                    _selectedCharacter.value = CharacterDetailsUiState.Error(error.message ?: "Unknown error")
                }
        }
    }
}
