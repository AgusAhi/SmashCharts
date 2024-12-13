package com.example.smashchartss

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.smashchartss.ui.theme.FontTittle
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import kotlinx.coroutines.launch
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterDetailsScreen(characterId: String, navHostController: NavHostController) {
    val allCharacters = remember { mutableStateOf<List<Character>>(emptyList()) }
    val availableCharacters = remember { mutableStateListOf<Character>() }
    val currentCharacter = allCharacters.value.find { it.id == characterId }

    // Lógica para obtener los datos del personaje
    CharacterFetchForDetails(
        characterId = characterId,
        allCharacters = allCharacters,
        availableCharacters = availableCharacters
    )

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
                navigationIcon = {
                    IconButton(onClick = { navHostController.navigateUp() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Go Back", tint = Color.White)
                    }
                },
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${currentCharacter?.name} details" ?: "Matchup Chart", // Título dinámico (Dynamic Title)
                            style = TextStyle(
                                fontFamily = FontTittle,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp,
                                color = Color.White
                            )
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Details for character: $characterId",
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                ),
                textAlign = TextAlign.Center
            )

        }
    }
}

@Composable
fun CharacterFetchForDetails(
    characterId: String,
    allCharacters: MutableState<List<Character>>,
    availableCharacters: SnapshotStateList<Character>
) {
    LaunchedEffect(key1 = characterId) {
        try {
            Log.d("CharacterFetchForDetails", "Fetching characters for ID: $characterId")
            val characters = fetchCharacters() // Implementa tu lógica aquí
            Log.d("CharacterFetchForDetails", "Characters fetched: ${characters.size}")

            allCharacters.value = characters
            availableCharacters.clear()
            availableCharacters.addAll(characters.filter { it.id != characterId })

            if (!characters.any { it.id == characterId }) {
                Log.e("CharacterFetchForDetails", "Character not found for ID: $characterId")
            }
        } catch (e: Exception) {
            Log.e("CharacterFetchForDetails", "Error fetching characters: ${e.message}")
        }
    }
}









